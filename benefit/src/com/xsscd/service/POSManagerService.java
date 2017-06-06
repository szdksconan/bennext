package com.xsscd.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.dao.CardInfoDao;
import com.xsscd.dao.POSManagerDao;
import com.xsscd.dao.RightRuleDao;
import com.xsscd.util.MsgUtil;
import com.xsscd.vo.VerifyStatus;

public class POSManagerService {
	private static Logger log = LoggerFactory.getLogger(Class.class);
	private final POSManagerDao pmdao = new POSManagerDao();

	/**
	 * 缴费接口业务处理
	 * 
	 * @param record
	 *            参数
	 * @return
	 */
	public String pay(final Record record) {
		try {
			final String cardNum = record.getStr("CardNum");
			final String rightID = record.getStr("RightID");
			final String phone = record.getStr("Phone");
			String dateStr = record.getStr("LocalTransDate");
			// int curCount = crdao.queryCountFromCardRight(cardNum, rightID);
			// final int payrule = crdao.findPayRuleFromCardRight(cardNum, rightID);
			Record rd = pmdao.findInfoFromCardRight(cardNum, rightID);
			if (rd == null) {
				return "|99|该卡没有分配该权益|";
			}
			int curCount = rd.getInt("Count");
			final int payrule = rd.getInt("PayRule");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String recordtime = sdf.format(new Time(System.currentTimeMillis()));
			String curDateMark = rd.getStr("DateMark");
			String dateMark = recordtime.substring(0, 4) + dateStr;
			int dailyNum = rd.getInt("DailyNum");
			int monthNum = rd.getInt("MonthNum");
			int yearNum = rd.getInt("YearNum");
			int dailyNumPerStore = rd.getInt("DailyNumPerStore");
			if (!curDateMark.equals(dateMark)) {// 如果日期标记不等，则更新DailyNumTop和DailyNumPerStore
				Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
				dailyNum = rightRd.getInt("DailyNumTop");
				dailyNumPerStore = rightRd.getInt("DailyNumPerStore");
				pmdao.updateDayNum(cardNum, rightID, dailyNum, dailyNumPerStore, dateMark);
			}
			// TODO 如果月份不等 更新
			if (!curDateMark.substring(0, 6).equals(dateMark.substring(0, 6))) {
				Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
				monthNum = rightRd.getInt("MonthNumTop");
				pmdao.updateMonthNum(cardNum, rightID, monthNum, dateMark);
			}
			// TODO 如果年份不等更新
			if (!curDateMark.substring(0, 4).equals(dateMark.substring(0, 4))) {
				Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
				yearNum = rightRd.getInt("YearNumTop");
				pmdao.updateYearNum(cardNum, rightID, yearNum, dateMark);
			}
			// TODO 判断该权益是否在规定服务时间
			Record service = pmdao.findServiceForRightId(rightID);
			Date curDate = new Date(), startDate = service.getDate("startDate"), endDate = service.getDate("endDate");
			// 权益失效
			if (service == null) {
				return "|99|该权益服务不存在|";
			} else if (startDate != null && curDate.compareTo(startDate) < 0) {
				return "|99|该权益服务时间还未开始|";
			} else if (endDate != null && curDate.compareTo(endDate) > 0) {
				return "|99|该权益服务时间已经过期|";
			}
			final int curDailyNum = dailyNum;
			final int curMonthNum = monthNum;
			final int curYearNum = yearNum;
			final Record cardinfo = pmdao.findCardInfoByCardNum(cardNum);
			if (curCount > 0) {
				final String supplyerName = pmdao.findSupplyerName(record.getStr("MerchantCode"),
						record.getStr("TermCode"));
				int todayNumThisStore = pmdao.findTodayCount(supplyerName, cardNum, rightID, dateStr);
				if (dailyNum == 0) {
					return "|99|当天该权益使用次数已达上限|";
				} else if (monthNum == 0) {
					return "|99|当月该权益使用次数已达上限|";
				} else if (yearNum == 0) {
					return "|99|当年该权益使用次数已达上限|";
				} else if (todayNumThisStore == dailyNumPerStore) {
					return "|99|本店当天该权益使用次数已达上限|";
				} else {
					boolean transRes = Db.tx(new IAtom() {
						@Override
						public boolean run() throws SQLException {
							int tmprule = payrule;
							if (payrule == 1) {// 当本次消费时是需要首次交费的时候
								tmprule = 3;
							}
							boolean result1 = true;
							if (!phone.equals("")) {
								result1 = pmdao.updatePhoneFromCardInfo(phone, cardNum);
							}
							boolean result2 = pmdao.pay(cardNum, rightID, 1, tmprule, curDailyNum - 1, curMonthNum - 1,
									curYearNum - 1);// 扣卡权益剩余次数，并且扣日剩余-月剩余-年剩余
							boolean result3 = pmdao.insertUsingRecord(record.remove("Phone")
									.set("RecordTime", recordtime).set("SupplyerName", supplyerName));// 记录权益使用信息
							return result1 && result2 && result3;
						}
					});
					if (transRes) {
						// //调用短信接口
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									String phonenum = phone.equals("") ? cardinfo.getStr("Phone") : phone;
									MsgUtil.sendMsgOnPaySuccess(phonenum, cardNum, rightID);
								} catch (NoSuchAlgorithmException e) {
									log.error("短信接口调用：MD5创建失败", e);
								} catch (IOException ex) {
									log.error("短信接口调用：url文件读取失败", ex);
								}
							}
						});
						t.start();
						if (cardinfo.getInt("CardTypeID") == -1) {
							return "|00|缴费成功|" + ((curCount - 1) > 10000 ? "" : (curCount - 1)) + "|欢迎体验银联白金卡权益|";
						} else {
							return "|00|缴费成功|" + ((curCount - 1) > 10000 ? "" : (curCount - 1)) + "|欢迎下次使用|";
						}

					} else {
						return "|99|系统出错|";
					}
				}
			} else {
				// 次数没有剩余则给非62卡返回|99|体验次数已用完|
				// 获取卡级别
				int cardType = cardinfo.getInt("CardTypeID");
				if (cardType == -1) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								String phonenum = phone.equals("") ? cardinfo.getStr("Phone") : phone;
								MsgUtil.sendMsgOnPayFailure(phonenum, cardNum, rightID);
							} catch (NoSuchAlgorithmException e) {
								log.error("短信接口调用：MD5创建失败", e);
							} catch (IOException ex) {
								log.error("短信接口调用：url文件读取失败", ex);
							}
						}
					});
					t.start();
					return "|99|体验次数已用完|";
				} else {
					return "|99|权益次数已用完|";
				}
			}
		} catch (Exception e) {
			log.error("缴费出错", e);
			return "|99|系统出错|";
		}
	}

	/**
	 * 冲正接口业务处理
	 * 
	 * @param record
	 * @return
	 */
	public String remedy(final Record record) {
		try {
			final String cardNum = record.getStr("CardNum");
			final String rightID = record.getStr("RightID");
			// 查询cardright表的数据
			Record rd = pmdao.findInfoFromCardRight(cardNum, rightID);
			int curDailyNum = rd.getInt("DailyNum");
			// int curDailyNumPerStore = rd.getInt("DailyNumPerStore");
			final int curCount = rd.getInt("Count");
			final int payrule = rd.getInt("PayRule");
			// 获取卡级别
			Record cardinfo = pmdao.findCardInfoByCardNum(cardNum);
			final int cardType = cardinfo.getInt("CardTypeID");
			// 查询rightrule表的数据

			Record rightcount = pmdao.findRightCount(rightID, cardType);
			final int ruleCount = rightcount.getInt("Count");

			Record rd2 = pmdao.findRightByID(rightID);
			int ruleDailyNumTop = rd2.getInt("DailyNumTop");
			final int rule1 = rd2.getInt("SelfRule1");
			if (curCount < ruleCount) {
				if (curDailyNum >= ruleDailyNumTop) {
					return "|99|冲正后每日使用次数超过上限|";
				} else {
					boolean transRes = Db.tx(new IAtom() {
						@Override
						public boolean run() throws SQLException {
							int tmprule = payrule;
							if ((curCount + 1) == ruleCount) {
								if (rule1 == 1 && cardType != -1) {
									tmprule = 1;
								}
							}
							boolean result1 = pmdao.remedy(cardNum, rightID, 1, tmprule);// 添加权益次数
							boolean result2 = pmdao.deleteUsingRecord(record);// 删除权益使用信息
							return result1 && result2;
						}
					});
					if (transRes) {
						return "|00|冲正成功|欢迎下次使用|";
					} else {
						return "|99|系统出错|";
					}
				}
			} else {
				return "|99|权益剩余可用次数已达上限|";
			}
		} catch (Exception e) {
			log.error("冲正出错", e);
			return "|99|系统出错|";
		}
	}

	/**
	 * 刷卡接口业务处理
	 * 
	 * @param rs
	 * @return
	 */
	public String consume(Record rs) {
		String respStr = "";
		try {
			String phone = rs.getStr("Phone");
			//
			String rightID = rs.getStr("RightID");
			String cardNum = rs.getStr("CardNum");
			String version = rs.getStr("Version");// 对方传过来的版本号，将用于与本地对应商户+终端的版本号匹配
			String termCode = rs.getStr("TermCode");
			String MerchantCode = rs.getStr("MerchantCode");

			// 刷卡步骤1：验证版本号

			Record supplyerinfo = pmdao.findSupplyerinfo(MerchantCode, termCode);
			if (supplyerinfo == null) {
				return "|99|该终端未配置|";
			}
			if (!version.equals(supplyerinfo.get("version"))) {// |M2|请更新参数|
				respStr = "|M2|请更新参数|";
				return respStr;
			}

			// 验证卡信息是否已经存在，不能存在则添加
			Record checkcard = pmdao.findCardInfoByCardNum(cardNum);
			boolean phoneflag = true;// true表示有手机号 false表示需要输入手机号

			// 刷卡步骤2-1：（无卡）验证或者记录卡信息

			if (checkcard == null) { // 如果系统中没有这张卡的信息
				// 判断手机号 存入卡片基本信息
				if ("".equals(phone)) {
					phoneflag = false;// 需要录入手机号
				}
				// 根据卡号找到对应cardbin的所有数据
				// List<Record> cardBins = pmdao.findCardBinInfo(cardNum);
				Record cardBin = CardInfoDao.dao.findCardBinInfo(cardNum);
				int cardType = -2;// -2表示其他类型的卡
				String bankID = "";
				if (cardBin == null) {// 非银联62标准卡
					Record other = pmdao.findOtherCardBinInfo(cardNum);
					if (other == null) {

						respStr = "|WK|该卡不能享受该权益|";
						return respStr;
					} else {
						if (other.getStr("cardClass").equals("贷记卡")) {
							cardType = -1;
							bankID = other.getStr("bankCode") == null ? "0" : other.getStr("bankCode");
						} else {
							respStr = "|WK|该卡不能享受该权益|";
							return respStr;
						}
					}
				} else {
					if (cardBin.getStr("cardClass").equals("贷记卡")) {
						cardType = cardBin.getInt("cardRank");
						bankID = cardBin.getStr("bankCode");
					} else {
						respStr = "|WK|该卡非银联白金贷记卡,不能享受此权益|";
						return respStr;
					}
				}
				// 不管62卡还是非62卡 都将卡信息存入数据库
				pmdao.addCardInfo(cardType, bankID, phone, cardNum);
			} else {// 有这张卡的信息
				// 刷卡步骤2-2：（有卡）更新手机号
				String ph = checkcard.get("Phone");
				if (ph == null || ph.equals("")) {// 以前没有记录手机
					// 判断手机号 修改卡片基本信息
					if ("".equals(phone)) {// 现在也没有手机号
						phoneflag = false;// 需要录入手机号
					} else {// 现在有手机号就更新
						pmdao.updatePhoneFromCardInfo(phone, cardNum);
					}
				} else if (phone != ph) {// 新的不等于旧的手机号
					if (!"".equals(phone)) {
						// 给数据库卡信息更新phone
						pmdao.updatePhoneFromCardInfo(phone, cardNum);
					}
				}
			}
			// 查询卡信息
			Record cardinfo = pmdao.findCardInfoByCardNum(cardNum);
			int cardType = cardinfo.getInt("CardTypeID");
			// String bankID = cardinfo.getStr("BankID");
			if (cardType < -1) {
				respStr = "|WK|该卡非银联白金卡，不能享受此权益|";
				return respStr;
			}
			// 刷卡步骤3-1： 3-1-0用于消费权益以及返回次数和所需金钱的变量
			Integer count = 0;
			int payrule = 0;
			int money = 0;
			// 3-1-1存在cardright，则直接进行刷卡流程

			// 看该卡对应的权益是否有记录，有则查询该权益剩余次数，次数为0 时，|99|剩余次数为0，不能享受优惠停车|
			Record crd = pmdao.findInfoFromCardRight(cardNum, rightID);
			if (crd != null) {
				count = crd.getInt("Count");
				payrule = crd.getInt("PayRule");
				money = crd.getInt("NeedMoney");
				if (count.equals(0)) {
					if (cardType == -1) {
						respStr = "|99|体验次数已用完|"; // 结束本次刷卡流程
						return respStr;
					} else {
						respStr = "|G3|次数已用完|"; // 结束本次刷卡流程
						return respStr;
					}
				} else {
					respStr = "|00|成功|" + (count > 10000 ? "" : count) + "|欢迎使用"
							+ pmdao.findCardTypeDescription(cardType) + "|";
				}
			} else {
				// 3-1-2 cardright表中没有这张卡的数据，则需要添加
				Record rightRd = pmdao.findRightByID(rightID);
				payrule = rightRd.getInt("SelfRule1");
				// int rule2 = rightRd.getInt("SelfRule2");
				money = rightRd.getInt("NeedMoney");
				Timestamp curtime = new Timestamp(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateMark = sdf.format(curtime);
				int dailyNum = rightRd.getInt("DailyNumTop");
				int monthNum = rightRd.getInt("MonthNumTop");
				int yearNum = rightRd.getInt("YearNumTop");
				int dailyNumPerStore = rightRd.getInt("DailyNumPerStore");

				Record rightcount = pmdao.findRightCount(rightID, cardType);
				count = 0;
				if (rightcount != null) {
					count = rightcount.getInt("Count");
				}
				if (count.equals(0)) {// 规则里没有给该权益分配次数
					if (cardType == -1) {
						respStr = "|99|非银联62标准卡无法享受该权益|";
					} else {
						respStr = "|WK|该卡不能享受该权益|";
					}
					return respStr;
				} else {// 添加cardright需要根据是否为62卡来分配次数
					if (cardType == -1) {// 非62卡，由于以后可能有其他规则，该部分暂不去除
						// 这里不使用rule2
						// switch (rule2) {
						// case 0:// 0表示无法享受权益
						// respStr = "|99|非62银联标准卡无法享受该权益|";
						// return respStr;
						// case 1:// 1表示非62卡可免费使用一次该权益（体验一次）
						// pmdao.addCardRight(cardNum, rightID, 1, 3, dailyNum, dailyNumPerStore, dateMark);
						// payrule = 3;// 非62卡体验次数免费
						// respStr = "|00|成功|1|欢迎使用银联卡|";
						// break;
						// default:
						// respStr = "|99|系统出错|";
						// return respStr;
						// }
						// FIXME 这里更改了非银联62标准卡使用方式

						// payrule = 3;// TODO 这里前台输入 非62卡体验次数免费
						pmdao.addCardRight(cardNum, rightID, count, payrule, dailyNum, monthNum, yearNum,
								dailyNumPerStore, dateMark);
						RightRuleDao.dao.editRightCountVID(Integer.valueOf(rightID), cardType,
								VerifyStatus.assignedRightRule.code);
						respStr = "|00|成功|" + count + "|欢迎使用银联卡|";
					} else {// 62卡
						// TODO 这里10000需要核实吗
						pmdao.addCardRight(cardNum, rightID, count, payrule, dailyNum, monthNum, yearNum,
								dailyNumPerStore, dateMark);
						RightRuleDao.dao.editRightCountVID(Integer.valueOf(rightID), cardType,
								VerifyStatus.assignedRightRule.code);
						respStr = "|00|成功|" + (count > 10000 ? "" : count) + "|欢迎使用"
								+ pmdao.findCardTypeDescription(cardType) + "|";
					}
				}
			}

			if (payrule != 3) {
				// 如果权益用完了
				String dateStr = rs.getStr("LocalTransDate");
				Record rd = pmdao.findInfoFromCardRight(cardNum, rightID);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				final String recordtime = sdf.format(new Time(System.currentTimeMillis()));
				String curDateMark = rd.getStr("DateMark");
				String dateMark = recordtime.substring(0, 4) + dateStr;
				int dailyNum = rd.getInt("DailyNum");
				int monthNum = rd.getInt("MonthNum");
				int yearNum = rd.getInt("YearNum");
				int dailyNumPerStore = rd.getInt("DailyNumPerStore");
				if (!curDateMark.equals(dateMark)) {// 如果日期标记不等，则更新DailyNumTop和DailyNumPerStore
					Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
					dailyNum = rightRd.getInt("DailyNumTop");
					dailyNumPerStore = rightRd.getInt("DailyNumPerStore");
					pmdao.updateDayNum(cardNum, rightID, dailyNum, dailyNumPerStore, dateMark);
				}
				// TODO 如果月份不等 更新
				if (!curDateMark.substring(0, 6).equals(dateMark.substring(0, 6))) {
					Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
					monthNum = rightRd.getInt("MonthNumTop");
					pmdao.updateMonthNum(cardNum, rightID, monthNum, dateMark);
				}
				// TODO 如果年份不等更新
				if (!curDateMark.substring(0, 4).equals(dateMark.substring(0, 4))) {
					Record rightRd = pmdao.findRightByID(rightID);// 更新这个数据无论成功与否都不用回滚
					yearNum = rightRd.getInt("YearNumTop");
					pmdao.updateYearNum(cardNum, rightID, yearNum, dateMark);
				}
				final String supplyerName = pmdao.findSupplyerName(rs.getStr("MerchantCode"), rs.getStr("TermCode"));
				int todayNumThisStore = pmdao.findTodayCount(supplyerName, cardNum, rightID, dateStr);
				if (dailyNum == 0) {
					return "|99|当天该权益使用次数已达上限|";
				} else if (monthNum == 0) {
					return "|99|当月该权益使用次数已达上限|";
				} else if (yearNum == 0) {
					return "|99|当年该权益使用次数已达上限|";
				} else if (todayNumThisStore == dailyNumPerStore) {
					return "|99|本店当天该权益使用次数已达上限|";
				}
				if (phoneflag) {
					respStr = "|TP|" + money + "|";
				} else {
					respStr = "|PL|" + money + "|";
				}
			} else {
				if (phoneflag) {
					String tmpStr = pay(rs.remove("Version"));// 调取本类的缴费接口进行扣除权益次数
					if (!tmpStr.contains("成功")) {
						if (tmpStr.contains("本店当天")) {
							respStr = "|99|本店当天该权益使用次数已达上限| ";
						} else if (tmpStr.contains("当天")) {
							respStr = "|99|当天该权益使用次数已达上限| ";
						} else {
							if (tmpStr.contains("体验次数已用完")) {
								respStr = "|99|体验次数已用完|";
							} else {
								respStr = "|99|刷卡失败|";
							}
						}
					} else {
						if (cardType == -1) {
							respStr = "|00|成功|" + ((count - 1) > 10000 ? "" : (count - 1)) + "|欢迎体验银联白金卡权益|";
						} else {
							respStr = "|00|成功|" + ((count - 1) > 10000 ? "" : (count - 1)) + "|欢迎使用"
									+ pmdao.findCardTypeDescription(cardType) + "|";
						}
					}
				} else {
					respStr = "|TL|请输入手机号| ";
				}
			}
			return respStr;

		} catch (Exception e) {
			respStr = "|99|系统出错|";
			log.error("刷卡出错", e);
		}
		return respStr;
	}

	/**
	 * 参数下载接口业务处理
	 * 
	 * @param rs
	 * @return
	 */
	public String download(Record rs) {
		String respStr = "";
		try {
			String TermCode = rs.getStr("TermCode");
			String merchantCode = rs.getStr("MerchantCode");
			Record supplyer = pmdao.findSupplyerInfo(merchantCode, TermCode);
			if (supplyer == null) {
				respStr = "|M7|该终端未配置|";
			} else {
				int supplyerID = supplyer.getInt("supplyerId");
				String version = supplyer.getStr("version");
				List<Record> list = pmdao.findSupplyerRight(supplyerID);
				if (list.size() != 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("|00|成功|" + version + "|" + list.size() + "|");
					for (Record record : list) {
						int RightID = record.getInt("RightID");
						String Display = record.getStr("Display");
						sb.append(RightID + "|" + Display + "|");
					}
					respStr = sb.toString();
				} else {
					respStr = "|M7|该终端未配置|";
				}
			}
		} catch (Exception e) {
			respStr = "|WF|请重新下载参数|";
			log.error("参数下载出错,查询商户的权益信息遇到问题", e);
		}
		return respStr;
	}
}

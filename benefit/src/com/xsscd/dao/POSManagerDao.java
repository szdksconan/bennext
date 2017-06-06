package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.vo.VerifyStatus;

public class POSManagerDao {
	/**
	 * 查询卡权益信息 cardright
	 * 
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @return
	 */
	public Record findInfoFromCardRight(String cardNum, String rightID) {
		String querysql = "select * from cardright cr left join rightinfo ri on  cr.RightID=ri.RightID where cr.CardNum=? and cr.RightID=?";
		Record record = Db.findFirst(querysql, cardNum, rightID);
		return record;
		// Record bin = CardInfoDao.dao.findCardBinInfo(cardNum);
		// bin = bin != null ? bin : CardInfoDao.dao.findOtherCardBinInfo(cardNum);
		// if (bin != null) {
		// Integer rank = bin.getInt("cardRank");
		// StringBuilder sql = new StringBuilder(" SELECT cr.* FROM cardright cr ");
		// sql.append(" LEFT JOIN rightinfo ri ON (cr.RightID = ri.RightID and ri.valid=" + VerifyStatus.valid.code
		// + ")");
		// sql.append(" INNER JOIN rightcount rc on (rc.RightID=ri.RightID and rc.valid=" + VerifyStatus.valid.code);
		// sql.append(" and rc.Rank=? and rc.VID in ( " + VerifyStatus.auditPassRightRule.code + ","
		// + VerifyStatus.assignedRightRule.code + ") )");
		// sql.append(" WHERE cr.CardNum =?");
		// sql.append(" AND cr.RightID =?");
		// // String querysql =
		// //
		// "select * from cardright cr left join rightinfo ri on  cr.RightID=ri.RightID where cr.CardNum=? and cr.RightID=?";
		// Record record = Db.findFirst(sql.toString(), rank, cardNum, rightID);
		// return record;
		// } else {
		// return null;
		// }
	}

	/**
	 * 添加卡权益信息
	 * 
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @param count
	 *            次数
	 * @param payRule
	 *            当前支付规则
	 * @param dailyNum
	 *            当前每日剩余可用次数
	 * @param dailyNumPerStore
	 *            当前单卡每日每店可用次数
	 * @param dateMark
	 *            当前日期标记
	 * @throws Exception
	 */
	public void addCardRight(String cardNum, String rightID, Integer count, int payRule, int dailyNum, int monthNum,
			int yearNum, int dailyNumPerStore, String dateMark) throws Exception {
		Record rd = new Record();
		rd.set("CardNum", cardNum);
		rd.set("RightID", rightID);
		rd.set("Count", count);
		rd.set("PayRule", payRule);
		rd.set("DailyNum", dailyNum);
		rd.set("MonthNum", monthNum);
		rd.set("YearNum", yearNum);
		rd.set("DailyNumPerStore", dailyNumPerStore);
		rd.set("DateMark", dateMark);
		boolean success = Db.save("cardright", rd);
		if (!success) {
			throw new Exception("addCardRight 添加卡权益信息出错");
		}
	}

	/**
	 * 查询权益信息
	 * 
	 * @param rightID
	 *            权益ID
	 * @return
	 */
	public Record findRightByID(String rightID) {
		String sql = "select * from rightinfo where RightID = ? and valid = 110";
		Record record = Db.findFirst(sql, rightID);
		return record;
	}

	/**
	 * 更新卡权益信息的每日消费上限
	 * 
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @param dailyNum
	 *            当日可消费次数，本处更新到最大值
	 * @param dailyNumPerStore
	 *            当日每店可消费次数
	 * @param dateMark
	 *            日期标记
	 * @return
	 */
	public boolean updateDayNum(String cardNum, String rightID, int dailyNum, int dailyNumPerStore, String dateMark) {
		String updatesql = "update cardright set DailyNum = ? ,DailyNumPerStore = ?,DateMark=? where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql, dailyNum, dailyNumPerStore, dateMark, cardNum, rightID);
	}

	public boolean updateMonthNum(String cardNum, String rightID, int monthNum, String dateMark) {
		String updatesql = "update cardright set MonthNum = ? ,DateMark=? where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql, monthNum, dateMark, cardNum, rightID);
	}

	public boolean updateYearNum(String cardNum, String rightID, int yearNum, String dateMark) {
		String updatesql = "update cardright set YearNum = ? ,DateMark=? where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql, yearNum, dateMark, cardNum, rightID);
	}

	/**
	 * 查询商户信息
	 * 
	 * @param TermCode
	 *            终端号
	 * @param merchantCode
	 *            商户号
	 * @return
	 */
	public Record findSupplyerInfo(String merchantCode, String TermCode) {
		String sql = "select * from supplyerinfo where merchantCode = ? and termCode = ? and state = 1";
		Record rd = Db.findFirst(sql, merchantCode, TermCode);
		return rd;
	}

	/**
	 * 查询商户名称
	 * 
	 * @param merchantCode
	 *            商户号
	 * @param termCode
	 *            终端号
	 * @return
	 */
	public String findSupplyerName(String merchantCode, String termCode) {
		String sql = "select SupplyerName from supplyerinfo where merchantCode = ? and termCode = ?";
		Record record = Db.findFirst(sql, merchantCode, termCode);
		return record.getStr("SupplyerName");
	}

	/**
	 * 查询某终端的版本信息
	 * 
	 * @param merchantCode
	 * @param termCode
	 * @return
	 */
	public Record findSupplyerinfo(String merchantCode, String termCode) {
		String sql = "select * from supplyerinfo where merchantCode=? and termCode = ?";
		return Db.findFirst(sql, merchantCode, termCode);
	}

	/**
	 * 查询商户权益
	 * 
	 * @param supplyerId
	 *            商户编号
	 * @return
	 */
	// 权益至少有一个规则通过审核
	public List<Record> findSupplyerRight(int supplyerId) {
		StringBuilder sql = new StringBuilder("SELECT r.* FROM service_supplyerinfo s");
		sql.append(" LEFT JOIN rightinfo r ON s.ServiceID = r.ServiceID");
		sql.append(" LEFT JOIN rightcount rc on rc.RightID=r.RightID");
		sql.append(" WHERE s.supplyerId = ?");
		sql.append(" AND s.valid = " + VerifyStatus.valid.code);
		sql.append(" AND r.valid = " + VerifyStatus.valid.code);
		sql.append(" AND rc.valid = " + VerifyStatus.valid.code);
		sql.append(" AND rc.VID in (" + VerifyStatus.assignedRightRule.code + ","
				+ VerifyStatus.auditPassRightRule.code + ")");
		sql.append(" GROUP BY r.RightID");
		sql.append(" HAVING Count(rc.Rank)>0");
		// String sql =
		// "select * from service_supplyerinfo s LEFT JOIN rightinfo r ON s.ServiceID = r.ServiceID where s.supplyerId = ? and s.valid = 110 and r.valid = 110";
		return Db.find(sql.toString(), supplyerId);
	}

	/**
	 * 查询某店今日消费次数
	 * 
	 * @param supplyerName
	 *            商户名称（店名）
	 * @param dateStr
	 * @param rightID
	 * @param cardNum
	 * @param string
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @param dateStr
	 *            日期标记，用于与LocalTransDate作比较
	 * @return
	 */
	public int findTodayCount(String supplyerName, String cardNum, String rightID, String dateStr) {
		String sql = "select COUNT(*) from usingrecord where SupplyerName=? and CardNum = ? and RightID = ? and LocalTransDate = ? and State = 1";
		Record rd = Db.findFirst(sql, supplyerName, cardNum, rightID, dateStr);
		return new Long(rd.getLong("COUNT(*)")).intValue();
	}

	/**
	 * 缴费，cardright表中对应次数减number次
	 * 
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @param number
	 *            扣除权益多少次
	 * @param rule
	 *            下一次的支付规则
	 * @param dailyNum
	 *            下一次的每日剩余次数
	 * @return
	 */
	public boolean pay(String cardNum, String rightID, int number, int rule, int dailyNum, int monthNum, int yearNum) {
		String updatesql = "update cardright set Count= (Count-?),PayRule = ?,DailyNum =?,MonthNum =?,YearNum =? where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql, number, rule, dailyNum, monthNum, yearNum, cardNum, rightID);
	}

	/**
	 * 新增权益消费记录（state=1）
	 * 
	 * @param record
	 * @return
	 */
	public boolean insertUsingRecord(Record record) {
		record.set("State", 1);
		return Db.save("usingrecord", record);
	}

	/**
	 * 删除权益消费记录（state=9）
	 * 
	 * @param record
	 * @return
	 */
	public boolean deleteUsingRecord(Record record) {
		String sql = "update usingrecord set State = 9 where TermCode = ?"
				+ " and MerchantCode = ? and LocalTransDate=? and LocalTransTime=?";
		return 1 == Db.update(sql, record.getStr("TermCode"), record.getStr("MerchantCode"),
				record.getStr("LocalTransDate"), record.getStr("LocalTransTime"));
	}

	/**
	 * 根据卡号查询卡信息
	 * 
	 * @param cardNum
	 *            卡号
	 * @return
	 */
	public Record findCardInfoByCardNum(String cardNum) {
		String sql = "select * from cardinfo where CardNum = ?";
		return Db.findFirst(sql, cardNum);
	}

	/**
	 * 添加卡信息
	 * 
	 * @param cardType
	 *            卡级别
	 * @param bankID
	 *            银行代码
	 * @param phone
	 *            手机号
	 * @param cardNum
	 *            卡号
	 * @throws Exception
	 */
	public void addCardInfo(int cardType, String bankID, String phone, String cardNum) throws Exception {
		Record rd = new Record();
		rd.set("BankID", bankID);
		rd.set("CardTypeID", cardType);
		rd.set("CardNum", cardNum);
		rd.set("Phone", phone);
		boolean success = Db.save("cardinfo", "CardNum", rd);
		if (!success) {
			throw new Exception("addCardInfo 添加卡信息出错");
		}
	}

	/**
	 * 更新卡信息中的手机号
	 * 
	 * @param phone
	 *            手机号
	 * @param cardNum
	 *            卡号
	 * @return
	 */
	public boolean updatePhoneFromCardInfo(String phone, String cardNum) {
		Record rd = new Record();
		rd.set("CardNum", cardNum);
		rd.set("Phone", phone);
		return Db.update("cardinfo", "CardNum", rd);
	}

	/**
	 * 查询权益对应级别应最高享用该权益多少次 rightcount
	 * 
	 * @param rightID
	 *            权益ID
	 * @param cardRank
	 *            卡级别
	 * @return
	 */
	// 访问rightcount时判断审核状态
	public Record findRightCount(String rightID, int cardRank) {
		String sql = "select * from rightcount where VID in (" + VerifyStatus.assignedRightRule.code + ","
				+ VerifyStatus.auditPassRightRule.code + ") and RightID = ? and Rank = ? and valid = 110";
		return Db.findFirst(sql, rightID, cardRank);
	}

	/**
	 * 冲正，将对应cardright的次数加number次
	 * 
	 * @param cardNum
	 *            卡号
	 * @param rightID
	 *            权益ID
	 * @param number
	 *            次数
	 * @param rule
	 *            消费规则更正
	 * @return
	 */
	public boolean remedy(String cardNum, String rightID, int number, int rule) {
		String updatesql = "update cardright set Count= (Count+?), DailyNum=(DailyNum+?),PayRule = ? where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql, number, number, rule, cardNum, rightID);
	}

	/**
	 * 通过卡号查询卡bin
	 * 
	 * @param cardNum
	 *            卡号
	 * @return
	 */
	// FIXME 有bug使用CardInfoDao findCardBinInfo代替
	// public List<Record> findCardBinInfo(String cardNum) {
	// String sql = "SELECT * from card_bin_info where cardBin= LEFT(?, Length(cardBin))";
	// List<Record> rs = Db.find(sql, cardNum);
	// return rs;
	// }

	/**
	 * 通过卡号查询other 卡bin
	 * 
	 * @param cardNum
	 * @return
	 */
	public Record findOtherCardBinInfo(String cardNum) {
		String sql = "SELECT * from card_bin_other_info where cardBin= LEFT(?, Length(cardBin))";
		return Db.findFirst(sql, cardNum);
	}

	/**
	 * 查询卡级别对应的描述信息 如cardtypeid=3 表示该卡为白金卡
	 * 
	 * @param cardTypeID
	 * @return
	 */
	public String findCardTypeDescription(int cardTypeID) {
		String sql = "select * from cardtype where CardTypeID = ?";
		Record rd = Db.findFirst(sql, cardTypeID);
		if (rd != null) {
			return rd.getStr("Description");
		} else {
			return "银联卡";
		}
	}

	public Record findServiceForRightId(String rightID) {
		StringBuilder sql = new StringBuilder(" select s.* from rightinfo ri ");
		sql.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
		sql.append(" where ri.valid=" + VerifyStatus.valid.code);
		sql.append(" and  s.valid=" + VerifyStatus.valid.code);
		sql.append(" and ri.RightID=?");
		return Db.findFirst(sql.toString(), rightID);
	}
}

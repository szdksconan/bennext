package com.xsscd.service;


import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.dao.CardRightDao;
import com.xsscd.dao.PosRightQueryDao;
import com.xsscd.dao.UsingRecordDao;

public class RightManagerService {
	private final CardRightDao crdao = new CardRightDao();
	private final UsingRecordDao urdao = new UsingRecordDao();
	public String pay(final Record record){
		try {
			final String cardNum = record.getStr("CardNum");
			final String rightID = record.getStr("RightID");
			int curCount = crdao.queryCountFromCardRight(cardNum, rightID);
			if(curCount >0){
				boolean transRes = Db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						boolean result1 = crdao.pay(cardNum, rightID, 1);//扣权益次数
						boolean result2 = urdao.insert(record);//记录权益使用信息
						return result1&&result2;
					}
				});
				if(transRes){
					//调用短信接口
//					Thread t=new Thread(new Runnable() {
//					@Override
//					public void run() {
//						HttpResponse response = HttpRequest
//						        .post("http://10.0.0.30:8080/GZ95516/AuthSend.action")
//						        .form("accessId", "10178",
//						                "receiver", "11219",
//						                "content  ", "a.zip",
//						                "sign", "11219")
//						        .send();
//						response.body();
//					}
//				});
//				t.start();
					return "|00|缴费成功|欢迎下次使用|";
				}else{
					return "|99|缴费失败|系统出错|";
				}
			}else{
				return "|99|缴费失败|权益已关闭|";
			}
		} catch (Exception e) {
			return "|99|缴费失败|系统出错|";
		}
	}
	public String remedy(final Record record){
		try {
			final String cardNum = record.getStr("CardNum");
			final String rightID = record.getStr("RightID");
			Record cardBin=PosRightQueryDao.dao.findCardNameAndBankCode(cardNum);
			Integer CardTypeID=PosRightQueryDao.dao.findCardTypeID(cardBin.getStr("bankCode"),cardBin.getStr("cardName"));
			int ruleCount = crdao.queryCountFromRightRule(rightID,CardTypeID);
			int curCount = crdao.queryCountFromCardRight(cardNum, rightID);
			if(curCount < ruleCount){
				boolean transRes = Db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						boolean result1 = crdao.remedy(cardNum, rightID, 1);//扣权益次数
						boolean result2 = urdao.delete(record);//删除权益使用信息
						return result1&&result2;
					}
				});
				if(transRes){
					//调用短信接口
//					Thread t=new Thread(new Runnable() {
//					@Override
//					public void run() {
//						HttpResponse response = HttpRequest
//						        .post("http://10.0.0.30:8080/GZ95516/AuthSend.action")
//						        .form("accessId", "10178",
//						                "receiver", "11219",
//						                "content  ", "a.zip",
//						                "sign", "11219")
//						        .send();
//						response.body();
//					}
//				});
//				t.start();
					return "|00|冲正成功|欢迎下次使用|";
				}else{
					return "|99|冲正失败|系统出错|";
				}
			}else{
				return "|99|冲正失败|权益剩余可用次数已达上限|";
			}
		} catch (Exception e) {
			return "|99|冲正失败|系统出错|";
		}
	}
}

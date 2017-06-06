package com.xsscd.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CardRightDao {
	/**
	 * 根据卡号和权益号查询“cardright”中权益剩余次数
	 * @param cardNum
	 * @param rightID
	 * @return
	 */
	public int queryCountFromCardRight(String cardNum,String rightID){
		String querysql = "select * from cardright where CardNum=? and RightID=?";
		Record record = Db.findFirst(querysql,cardNum,rightID);
		return record.getInt("Count");
	}
	/**
	 * 扣权益（消费权益）
	 * @param cardNum
	 * @param rightID
	 * @param number
	 * @return
	 */
	public boolean pay(String cardNum,String rightID,int number){
		String updatesql = "update cardright set Count= (Count-?) where CardNum=? and RightID=?";
		return 1==Db.update(updatesql,number,cardNum,rightID);
	}
	/**
	 * 根据权益好查询“rightrule”中的次数
	 * @param rightID
	 * @return
	 */
	public int queryCountFromRightRule(String rightID,Integer CardTypeID){
		String querysql = "select * from rightrule where RightID=? and CardTypeID =?";
		Record record = Db.findFirst(querysql,rightID,CardTypeID);
		return record.getInt("Count");
	}
	/**
	 * 加权益（冲正）
	 * @param cardNum
	 * @param rightID
	 * @param number
	 * @return
	 */
	public boolean remedy(String cardNum,String rightID,int number){
		String updatesql = "update cardright set Count= (Count+?) where CardNum=? and RightID=?";
		return 1==Db.update(updatesql,number,cardNum,rightID);
	}
}

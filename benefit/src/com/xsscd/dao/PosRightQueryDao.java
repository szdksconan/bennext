package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class PosRightQueryDao {

	public static PosRightQueryDao dao =new PosRightQueryDao();

	/**
	 * 查询商户的权益列表
	 * @param supplyerID
	 * @return
	 */
	public List<Record> findSupplyerRight(String supplyerID) {
		String sql="select ri.RightID,ri.Display from rightinfo ri where ri.SupplyerID=?";
		return Db.find(sql, supplyerID);
	}

	/**
	 * 是否需要钱
	 * @param RightID
	 * @return
	 */
	public Integer isNeedMoney(String RightID) {
		String sql="select ri.NeedMoney from rightinfo ri where ri.RightID=?";
		Record rd=Db.findFirst(sql, RightID);	
		return rd.getInt("NeedMoney");
	}
	/**
	 * 查询卡信息
	 * 如果有看是否有手机号，有的话不需要输入，没有则需要输入
	 */
	public Record findCardInfo(String cardNum){
		String sql="select ci.Phone from cardinfo ci where ci.CardNum=?";
		return Db.findFirst(sql,cardNum);
	}	
	/**
	 *更新手机号 
	 * @param phone
	 * @param cardNum
	 */
	public void editCardInfo(String phone, String cardNum)throws Exception {
//		String updatesql = "update cardinfo set Phone="+phone+" where CardNum='"+cardNum+"'";
		Record rd =new Record();
		rd.set("CardNum", cardNum);
		rd.set("Phone", phone);
		boolean success= Db.update("cardinfo", "CardNum", rd);
		if(!success){
			throw new Exception("editCardInfo 修改卡信息出错");
		}
	}
	/**
	 * 添加卡信息
	 * @param phone
	 * @param cardNum
	 */
	public void addCardInfo(String phone, String cardNum)throws Exception {
		Record rd =new Record();
		rd.set("CardNum", cardNum);
		rd.set("Phone", phone);
		boolean success= Db.save("cardinfo", "CardNum", rd);
		if(!success){
			throw new Exception("addCardInfo 添加卡信息出错");
		}
	}
	/**
	 * 是否存在
	 */
	public boolean isExistCardRightRule(String cardNum, String rightID) {
		String sql="select cr.RightID from cardright cr where cr.CardNum=? and cr.RightID=?";
		Record rd=Db.findFirst(sql, cardNum,rightID);
		return rd==null?false:true;
	}
	/**
	 * 查询数量和权益名称
	 */
	public Record findCardRightInfo(String cardNum, String rightID) {
		StringBuilder sql= new StringBuilder("select cr.Count,ri.Display from cardright cr "); 
				sql.append(" LEFT JOIN rightinfo ri on  cr.RightID=ri.RightID ");
				sql.append(" where cr.CardNum=? and cr.RightID=?");
		return Db.findFirst(sql.toString(), cardNum,rightID);
	}
	/**
	 * 添加卡的权益信息
	 * @param cardNum
	 * @param rightID
	 * @param count
	 */
	public void addCardRight(String cardNum, String rightID, Integer count) throws Exception{
		Record rd =new Record();
		rd.set("CardNum", cardNum);
		rd.set("RightID", rightID);
		rd.set("Count", count);
		boolean success= Db.save("cardright", rd);
		if(!success){
			throw new Exception("addCardInfo 添加卡信息出错");
		}
	}

	public Record findCardNameAndBankCode(String cardNum) {
		String sql="SELECT cbi.cardName,cbi.bankCode from card_bin_info cbi where cbi.cardBin= LEFT(?, cbi.cardBinLength)";
		Record rd=Db.findFirst(sql, cardNum);
		return rd;
	}
	public Integer findCardTypeID(String BankID,String Description){
		String sql="select ct.CardTypeID from cardtype ct where ct.BankID=? and ct.Description=?";
		Record rd=Db.findFirst(sql, BankID,Description);
		return rd.getInt("CardTypeID");
	}
	/**
	 * 查询卡和权益对应的规则的数量
	 * @return
	 */
	public Integer findCardRightRuleCount(Integer cardTypeID, String bankId,
			String rightID) {
		String sql="SELECT rr.Count from rightrule rr where rr.CardTypeID=? and rr.BankID=? and rr.RightID=?";
		Record rd=Db.findFirst(sql, cardTypeID,bankId,rightID);
		return rd.getInt("Count");
	}

	
}

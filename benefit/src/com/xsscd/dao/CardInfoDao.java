package com.xsscd.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class CardInfoDao {
	public static CardInfoDao dao = new CardInfoDao();

	// public static void main(String[] args) {
	// Db.execute(new ICallback() {
	// @Override
	// public Object call(Connection conn) throws SQLException {
	// CallableStatement cs = conn.prepareCall("");
	// cs.setString(1, "2131");
	// cs.registerOutParameter(2, Types.VARCHAR);
	// cs.execute();
	// String ss = cs.getString(2);
	// cs.getResultSet();
	// // TODO Auto-generated method stub
	// return null;
	// }
	// });
	//
	// }

	/**
	 * 查询卡名称和银行
	 * 
	 * @param cardNum
	 * @return
	 */
	public Record findBankAndCardName(String cardNum) {
		StringBuilder sql = new StringBuilder(" SELECT * from ( ");
		sql.append(" SELECT bankName,cardName,cardClass from card_bin_info where cardBin= LEFT(?, Length(cardBin))");
		sql.append(" and SUBSTRING(?,cardStart,rangLength)>=cardBeginRange");
		sql.append(" and SUBSTRING(?,cardStart,rangLength)<=cardEndRange");
		sql.append(" UNION all");
		sql.append(" SELECT bankName,cardName,cardClass from card_bin_other_info where cardBin= LEFT(?, Length(cardBin))");
		sql.append(" ) t");
		sql.append(" GROUP BY bankName,cardName,cardClass");
		Record rd = Db.findFirst(sql.toString(), cardNum, cardNum, cardNum, cardNum);
		return rd;
	}

	/**
	 * 通过卡号查询卡bin
	 * 
	 * @param cardNum
	 *            卡号
	 * @return
	 */
	public Record findCardBinInfo(String cardNum) {
		// String sql="SELECT * from card_bin_info where cardBin= LEFT(?, Length(cardBin))";
		StringBuilder sql = new StringBuilder(" SELECT * from card_bin_info where cardBin= LEFT(?, Length(cardBin))");
		sql.append(" and SUBSTRING(?,cardStart,rangLength)>=cardBeginRange");
		sql.append(" and SUBSTRING(?,cardStart,rangLength)<=cardEndRange");
		sql.append(" and cardClass='贷记卡'");
		return Db.findFirst(sql.toString(), cardNum, cardNum, cardNum);
	}

	/**
	 * 通过卡号查询other 卡bin
	 * 
	 * @param cardNum
	 * @return
	 */
	public Record findOtherCardBinInfo(String cardNum) {
		String sql = "SELECT * from card_bin_other_info where cardBin= LEFT(?, Length(cardBin))  and cardClass='贷记卡'";
		return Db.findFirst(sql, cardNum);
	}

	/**
	 * 查询卡类型
	 * 
	 * @param BankID
	 * @param Description
	 * @return
	 */
	// public Integer findCardTypeID(String BankID,String Description){
	// String sql="select ct.CardTypeID from cardtype ct where ct.BankID=? and ct.Description=?";
	// Record rd=Db.findFirst(sql, BankID,Description);
	// return rd.getInt("CardTypeID");
	// }
	/**
	 * 添加或修改卡信息
	 * 
	 * @param phone
	 * @param cardNum
	 */
	public boolean addOrEditCardInfo(Record rd) throws Exception {
		Record oldCard = findCardInfo(rd.getStr("CardNum"));
		if (oldCard == null) {
			return Db.save("cardinfo", "CardNum", rd);
		} else {
			return Db.update("cardinfo", "CardNum", rd);
		}

	}

	public boolean editCardInfo(Record cardinfo) {
		return Db.update("cardinfo", "CardNum", cardinfo);
	}

	public Record findCardInfo(String cardNum) {
		return Db.findFirst("select * from cardinfo ci where ci.CardNum=?", cardNum);
	}
}

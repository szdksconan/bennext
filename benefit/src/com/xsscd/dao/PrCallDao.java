package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class PrCallDao {
	public static PrCallDao dao = new PrCallDao();	

	//查询卡权益信息
	public List<Record> findCardRightByCardNum(String cardNum) {
		StringBuilder querysql = new StringBuilder(
					" SELECT t.CardNum cardNum,t.CardTypeID cardTypeId,t.RightID rightId,t.Count count,t.Display display,t.ServiceID serviceId,t.serviceName,IFNULL(t1.Count,t.count) residueDegree FROM ");		
		querysql.append("(");
		querysql.append("  SELECT a.CardNum,a.CardTypeID,b.RightID,b.Count,c.Display,c.ServiceID,d.serviceName ");
		querysql.append("    FROM cardinfo a, rightcount b,rightinfo c,service d ");
		querysql.append("   WHERE a.CardTypeID = b.Rank AND b.RightID = c.RightID AND c.ServiceID = d.serviceId AND DATE_FORMAT(NOW(),'%Y-%c-%d %T') BETWEEN d.startDate AND d.endDate" );		
		querysql.append("     AND a.cardnum = ? ");	
		querysql.append(") t ");
		querysql.append("LEFT JOIN cardright t1 ON t.CardNum = t1.CardNum AND t.RightID = t1.RightID");
		//执行查询
		List<Record> rightList = Db.find(querysql.toString(), cardNum);
		return rightList;
	}

	//查询权益可受理商户
	public List<Record> findMerCardRightByMer(String merchantCode,String rightID) {
		StringBuilder querysql = new StringBuilder(
					"   SELECT a.RightID rightId,b.ServiceID servcieId,b.serviceName serviceName,a.Display display,d.supplyerName supplyerName,d.merchantCode,d.termCode,d.address,d.longitude,d.latitude  ");		
		querysql.append(" FROM rightinfo a,service b,service_supplyerinfo c,supplyerinfo d ");
		querysql.append("WHERE a.ServiceID = b.serviceId AND b.serviceId = c.serviceId AND c.supplyerId = d.supplyerId ");	
		querysql.append("  AND d.merchantCode =  ? AND a.RightID = ? ");	
		//执行查询
		List<Record> merList = Db.find(querysql.toString(), merchantCode ,rightID);
		return merList;
	}
	
	/**
	 * 查询在此商户今日消费次数
	 * 
	 * @param supplyerName 商户名称（店名）
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
	public int findMerTodayCount(String supplyerName, String cardNum, String rightID, String dateStr) {
		String sql = "select COUNT(*) from usingrecord where SupplyerName=? and CardNum = ? and RightID = ? and LocalTransDate = ? and State in ('0','1')";
		Record rd = Db.findFirst(sql, supplyerName, cardNum, rightID, dateStr);
		return new Long(rd.getLong("COUNT(*)")).intValue();
	}

	//查询预提交数据
	public Record findUsingRecord(String sysTrackNum,String retRefNum) {
		String sql = "select * from usingrecord where SysTrackNum = ? and RetRefNum = ?";
		return Db.findFirst(sql, sysTrackNum,retRefNum);
	}

	//回滚预提交数据
	public boolean rollbackConsume(String cardNum, String rightID,String sysTrackNum,String retRefNum) {
		String updatesql = "update cardright set Count= Count+1,DailyNum =DailyNum+1,MonthNum =MonthNum+1,YearNum =YearNum+1 where CardNum=? and RightID=?";
		return 1 == Db.update(updatesql,cardNum, rightID);
	}

	//新增权益预消费记录（state=0）
	public boolean insertPreUsingRecord(Record record) {
		record.set("State", 0);
		return Db.save("usingrecord", record);
	}	

	//更新权益消费记录（0：预提交 1:提交 9:作废）
	public boolean updateUsingRecord(Record record) {
		String sql = "update usingrecord set State = ? where SysTrackNum = ? and RetRefNum = ?";
		return 1 == Db.update(sql, record.getStr("State"), record.getStr("SysTrackNum"),record.getStr("RetRefNum"));
	}
}

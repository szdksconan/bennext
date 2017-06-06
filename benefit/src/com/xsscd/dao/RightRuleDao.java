package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.vo.VerifyStatus;

public class RightRuleDao {

	public static RightRuleDao dao = new RightRuleDao();

	public boolean add(Record record) throws Exception {
		return Db.save("rightrule", "RuleID", record);
	}

	public boolean edit(Record record) throws Exception {
		return Db.update("rightrule", "RuleID", record);
	}

	public boolean delete(Object id) throws Exception {
		return Db.deleteById("rightrule", "RuleID", id);
	}

	// 供应方机构 权益类型 权益名称
	public Page<Record> find(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" SELECT rr.RuleID,ri.RightID,ri.Display,bi.BankID,bi.BankName,");
		sql1.append(" ct.CardTypeID,ct.Description,rr.Count,vs.VDescription ");
		StringBuilder sql2 = new StringBuilder(" from rightrule rr");
		sql2.append(" LEFT JOIN rightinfo ri on rr.RightID=ri.RightID");
		sql2.append(" LEFT JOIN bankinfo bi on rr.BankID=bi.BankID");
		sql2.append(" LEFT JOIN cardtype ct on rr.CardTypeID=ct.CardTypeID");
		sql2.append(" LEFT JOIN verifystatus vs on rr.VID=vs.VID");
		sql2.append(" where 1=1 ");
		// 关联规则相关状态(4,5,6)
		String vids = params.get("vids");
		if (StringUtil.isNotBlank(vids)) {
			sql2.append(" and vs.VID in (");
			String[] vidArr = vids.split(",");
			for (String vid : vidArr) {
				sql2.append("?,");
				param.add(vid);
			}
			sql2.deleteCharAt(sql2.length() - 1);
			sql2.append(")");
		}
		String rightName = params.get("rightName");
		if (StringUtil.isNotBlank(rightName)) {
			sql2.append(" and ri.Display like ?");
			param.add("%" + rightName + "%");
		}
		String bankName = params.get("bankName");
		if (StringUtil.isNotBlank((String) params.get("bankName"))) {
			sql2.append(" and bi.BankName like ?");
			param.add("%" + bankName + "%");
		}
		String cardTypeName = params.get("cardTypeName");
		if (StringUtil.isNotBlank(cardTypeName)) {
			sql2.append(" and ct.Description like ?");
			param.add("%" + cardTypeName + "%");
		}
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	public List<Record> findAuditPassRightIdName() {
		String sql = "select ri.RightID,ri.Display from rightinfo ri where VID=101";
		return Db.find(sql);
	}

	public List<Record> findBankIdName() {
		String sql = "select bi.BankID,bi.BankName from bankinfo bi ";
		return Db.find(sql);
	}

	public List<Record> findCardTypeIdName(String bankID) {
		String sql = "select ct.CardTypeID,ct.Description from cardtype ct where ct.BankID=?";
		return Db.find(sql, bankID);
	}

	/**
	 * -------------------------------------新版本----------------------------------------------------
	 */
	public Page<Record> findRightRule(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" select s.serviceName,s.startDate,s.endDate,ri.Display,ri.RightID,");
		sql1.append(" ri.NeedMoney/100 NeedMoney,ri.DailyNumTop,ri.DailyNumPerStore,");
		sql1.append(" CONCAT(ct.Description,'(',ct.CardType,')') cardType,ct.CardTypeID, rc.Count,vs.VDescription");
		StringBuilder sql2 = new StringBuilder(" from rightcount rc");
		sql2.append(" LEFT JOIN rightinfo ri on rc.RightID=ri.RightID");
		sql2.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
		sql2.append(" LEFT JOIN cardtype ct on rc.Rank=ct.CardTypeID");
		sql2.append(" LEFT JOIN verifystatus vs on rc.VID=vs.VID");
		sql2.append(" where 1=1 ");
		sql2.append(" and rc.valid=" + VerifyStatus.valid);
		sql2.append(" and ri.valid=" + VerifyStatus.valid);
		sql2.append(" and s.valid=" + VerifyStatus.valid);
		String rightName = params.get("rightName");
		if (StringUtil.isNotBlank(rightName)) {
			sql2.append(" and ri.Display like ?");
			param.add("%" + rightName + "%");
		}
		String serviceName = params.get("serviceName");
		if (StringUtil.isNotBlank(serviceName)) {
			sql2.append(" and s.serviceName like ?");
			param.add("%" + serviceName + "%");
		}
		sql2.append(" ORDER BY rc.VID ");
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	public boolean audit(Integer VID, Integer RightID, Integer Rank) throws Exception {
		String sql = "UPDATE rightcount set VID=? where RightID=? and Rank=?";
		return 1 == Db.update(sql, VID, RightID, Rank);
	}

	/**
	 * 查询临时表数据
	 */
	public List<Record> findTemp(Integer... vID) {
		StringBuilder sql = new StringBuilder("select * from rightcount_temp rct where 1=1 ");
		if (vID.length > 0) {
			sql.append(" and rct.VID in (");
			for (int i = 0; i < vID.length; i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		return Db.find(sql.toString(), vID);
	}

	/**
	 * 为所有满足卡类型的卡用户分配对应的权益 如果已经有该权益则忽略
	 * 
	 * @param rightID
	 * @param rank
	 * @param count
	 * @return
	 */
	public boolean allocationRightCount(Integer rightID, Integer rank, Integer count) {

		StringBuilder sql = new StringBuilder(
				" INSERT INTO cardright (CardNum,RightID,Count,PayRule,DailyNum,DailyNumPerStore,DateMark)");
		sql.append(" SELECT * from ( SELECT ");
		sql.append(" ci.CardNum,? rightId,? count,ri.SelfRule1,ri.DailyNumTop,ri.DailyNumPerStore,DATE_FORMAT(CURDATE(),'%Y%m%d') ");
		sql.append(" FROM cardinfo ci LEFT JOIN rightinfo ri on ri.RightID=?");
		sql.append(" where ci.CardTypeID=? and  1!=(select count(*) from cardright cr where cr.RightID=? and cr.cardNum=ci.CardNum)) t");
		return 0 <= Db.update(sql.toString(), rightID, count, rightID, rank, rightID);
	}

	/**
	 * 删除temp
	 * 
	 * @param rightID
	 * @param rank
	 * @return
	 */
	public boolean deleteRightCountTemp(Integer rightID, Integer rank) {
		String sql = "delete from rightcount_temp where RightID=? and Rank=? ";
		return 1 == Db.update(sql, rightID, rank);
	}

	/**
	 * 修改rightcount审核状态
	 */

	public boolean editRightCountVID(Integer rightID, Integer rank, Integer VID) {
		String sql = "UPDATE rightcount set VID=? where RightID=? and Rank=?";
		return 1 == Db.update(sql, VID, rightID, rank);
	}

	/**
	 * 为所有满足卡类型的卡用户删除卡权益
	 * 
	 * @param rightID
	 * @param rank
	 * @return
	 */
	public boolean deleteCardRight(Integer rightID, Integer rank) {
		StringBuilder sql = new StringBuilder(" DELETE cr.* FROM cardinfo ci");
		sql.append(" LEFT JOIN  cardright cr on ci.CardNum=cr.CardNum");
		sql.append(" where ci.CardTypeID=? and  cr.RightID=?");
		return 0 <= Db.update(sql.toString(), rank, rightID);
	}

	/**
	 * copy rightcount数据到temp临时表
	 * 
	 * @param rightID
	 * @param rank
	 * @return
	 */
	public boolean copyRightCountForTemp(Integer rightID, Integer rank) {
		StringBuilder sql = new StringBuilder(" INSERT into rightcount_temp(RightID,Rank,Count,VID)");
		sql.append(" SELECT * from( SELECT rc.RightID,rc.Rank,rc.Count,rc.VID ");
		sql.append(" from rightcount rc where rc.RightID=? and rc.Rank=? ");
		sql.append(" and 1!=(select count(*) from rightcount_temp rct where rct.RightID=? and rct.Rank=?) )t");
		return 1 == Db.update(sql.toString(), rightID, rank, rightID, rank);
	}

	/**
	 * 添加所有需要分配的权益到临时表
	 */
	public void addTempforAllAuditPass() {
		StringBuilder sql = new StringBuilder(" INSERT into rightcount_temp(RightID,Rank,Count,VID)");
		sql.append(" SELECT t.rightId,t.rank,t.count,t.vid from (");
		sql.append(" SELECT rc.RightID rightId,rc.Rank rank,rc.Count count,rc.VID vid");
		sql.append(" from rightcount rc");
		sql.append(" where rc.VID=" + VerifyStatus.auditPassRightRule.code + " or rc.VID="
				+ VerifyStatus.assignedRightRule.code);
		sql.append(" union all ");
		sql.append(" select rct.RightID,rct.Rank,rct.Count,rct.VID from rightcount_temp rct");
		sql.append(" )t");
		sql.append(" GROUP BY t.rightId,t.rank");
		sql.append(" HAVING count(*)=1");
	}
}
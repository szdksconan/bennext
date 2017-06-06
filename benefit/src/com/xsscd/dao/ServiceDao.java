package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.exception.EditEntityException;
import com.xsscd.vo.VerifyStatus;

public class ServiceDao {

	public static ServiceDao dao = new ServiceDao();

	/**
	 * 添加服务
	 */
	public boolean add(Record record) throws Exception {
		return Db.save("service", "serviceId", record);
	}

	public void editServiceSupplyerInfoInvalid(Integer serviceId) {
		// String sql="DELETE FROM service_supplyerinfo WHERE serviceId=?";
		String sql = "UPDATE service_supplyerinfo set valid=? where serviceId=?";
		Db.update(sql, VerifyStatus.invalid.code, serviceId);
	}

	/**
	 * 添加服务和商户的关系信息
	 */
	public boolean addServiceSupplyerInfo(Integer serviceId, Integer supplyerId) {
		String sql = "INSERT INTO service_supplyerinfo(serviceId,supplyerId,valid) VALUES(?,?,?)";
		return 1 == Db.update(sql, serviceId, supplyerId, VerifyStatus.valid.code);
	}

	/**
	 * 添加或修改服务和商户的关系信息
	 */
	public boolean addOrEditServiceSupplyerInfo(Integer serviceId, Integer supplyerId) {
		String selSql = " select serviceId from service_supplyerinfo where serviceId=? and supplyerId=?";
		if (Db.findFirst(selSql, serviceId, supplyerId) == null) {
			String sql = "INSERT INTO service_supplyerinfo(serviceId,supplyerId,valid) VALUES(?,?,?)";
			return 1 == Db.update(sql, serviceId, supplyerId, VerifyStatus.valid.code);
		} else {
			String sql = "UPDATE service_supplyerinfo set valid=? where serviceId=? and supplyerId=?";
			return 1 == Db.update(sql, VerifyStatus.valid.code, serviceId, supplyerId);
		}
	}

	/**
	 * 添加权益
	 */
	public boolean addRightInfo(Record rd) {
		return Db.save("rightinfo", "RightID", rd);
	}

	/**
	 * 修改权益 -已经使用的权益不能修改（注：cardright用到 了权益信息）
	 */
	public boolean editRightInfo(Record rd) throws Exception {
		// 查询权益是否有规则在使用
		StringBuilder sql = new StringBuilder("select rc.RightID,rc.Rank from rightcount rc where rc.RightID=?");
		sql.append(" and rc.valid=" + VerifyStatus.valid.code + " and rc.VID in(");
		sql.append(VerifyStatus.auditPassRightRule.code + "," + VerifyStatus.stopUsingRightRule.code + ","
				+ VerifyStatus.assignedRightRule.code + "  )");
		List<Record> list = Db.find(sql.toString(), rd.get("RightID"));
		if (list.size() != 0) {
			throw new EditEntityException("该权益有规则在使用不能修改");
		}
		return Db.update("rightinfo", "RightID", rd);
	}

	/**
	 * 
	 */
	public List<Record> findRightRulelistForRightIdsAndVIds(List<Integer> needRightIds, Integer serviceId,
			List<Integer> VIDs) {
		StringBuilder sql = new StringBuilder("select ri.Display,ct.Description,vs.VDescription from rightinfo ri ");
		sql.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
		sql.append(" LEFT JOIN rightcount rc on rc.RightID=ri.RightID");
		sql.append(" LEFT JOIN cardtype ct on rc.Rank=ct.CardTypeID");
		sql.append(" LEFT JOIN verifystatus vs on rc.VID=vs.VID");
		sql.append(" where 1=1");
		sql.append(" and rc.VID in (");
		for (int i = 0; i < VIDs.size(); i++) {
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		sql.append(" and ri.RightID not in (");
		for (Integer rightId : needRightIds) {
			sql.append("?,");
			VIDs.add(rightId);
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		sql.append(" and s.serviceId=?");
		VIDs.add(serviceId);
		return Db.find(sql.toString(), VIDs.toArray());
	}

	/**
	 * 如果权益规则是已通过、已分配、停用不能删除 查询不能删除的权益规则
	 * 
	 * @param serviceId
	 */
	public List<Record> findRightRulelistForServiceIdAndVIds(Integer serviceId, List<Integer> VIDs) {
		StringBuilder sql = new StringBuilder(
				" select ri.Display rightName,ct.Description cardType,vs.VDescription from service s");
		sql.append(" LEFT JOIN rightinfo ri on ri.ServiceID= s.serviceId");
		sql.append(" LEFT JOIN rightcount rc on rc.RightID=ri.RightID");
		sql.append(" LEFT JOIN cardtype ct on rc.Rank=ct.CardTypeID");
		sql.append(" LEFT JOIN verifystatus vs on rc.VID=vs.VID");
		sql.append(" where 1=1");
		sql.append(" and rc.VID in (");
		for (int i = 0; i < VIDs.size(); i++) {
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		sql.append(" and  s.serviceId=?");
		VIDs.add(serviceId);
		return Db.find(sql.toString(), VIDs.toArray());
	}

	/**
	 * 修改不需要的权益及权益规则失效状态
	 * 
	 * @param needrights
	 */
	public void editRightInfoAndRightRuleInvalid(List<Integer> needrights, Integer serviceId) {
		// 修改权益为失效
		String opType = " update rightinfo set valid=" + VerifyStatus.invalid.code;
		StringBuilder sql = new StringBuilder(" WHERE 1=1");
		if (needrights.size() != 0) {
			sql.append(" and RightID not in(");
			for (int i = 0; i < needrights.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		sql.append(" and ServiceID=?");
		needrights.add(serviceId);
		Db.update(opType + sql.toString(), needrights.toArray());
		// 修改权益规则为失效
		StringBuilder sql2 = new StringBuilder("update rightcount set valid=" + VerifyStatus.invalid.code
				+ " where RightID in( select RightID FROM rightinfo ");
		sql2.append(sql.toString());
		sql2.append(")");
		Db.update(sql2.toString(), needrights.toArray());
	}

	/**
	 * 添加权益规则
	 */
	public boolean addRightRule(Integer RightID, Integer Rank, Integer Count, Integer VID) {
		String sql = "INSERT INTO rightcount(RightID,Rank,Count,VID,valid) VALUES(?,?,?,?,?)";
		return 1 == Db.update(sql, RightID, Rank, Count, VID, VerifyStatus.valid.code);
	}

	/**
	 *
	 */
	public boolean addOrEditRightRule(Integer RightID, Integer Rank, Integer Count) throws EditEntityException {
		String selSql = "select Count,VID,RightID from rightcount where RightID=? and Rank=?";
		Record rd = Db.findFirst(selSql, RightID, Rank);
		if (rd == null) {
			String sql = "INSERT INTO rightcount(RightID,Rank,Count,VID,valid) VALUES(?,?,?,?,?)";
			return 1 == Db.update(sql, RightID, Rank, Count, VerifyStatus.noAuditRightRule.code,
					VerifyStatus.valid.code);
		} else if (!VerifyStatus.noAuditRightRule.code.equals(rd.getInt("VID"))
				|| !VerifyStatus.auditNoPassRightRule.code.equals(rd.getInt("VID"))
				|| !VerifyStatus.stopUsingCompleteRightRule.code.equals(rd.getInt("VID"))) {
			if (rd.getInt("Count").equals(Count)) {
				return true;
			}
			// 当前规则不能修改
			throw new EditEntityException("权益规则的状态不能修改");
		} else {
			String sql = "UPDATE rightcount set Count=?,VID=? where RightID=? and Rank=?";
			Integer vid = rd.getInt("Count").equals(Count) ? rd.getInt("VID") : VerifyStatus.noAuditRightRule.code;
			return 1 == Db.update(sql, Count, vid, RightID, Rank);
		}
	}

	public boolean edit(Record record) throws Exception {
		return Db.update("service", "serviceId", record);
	}

	// 服务失效
	public boolean delete(Integer id) throws Exception {
		String sql = "UPDATE service set valid=? where serviceId=?";
		return 1 == Db.update(sql, VerifyStatus.invalid.code, id);
	}

	// 供应方机构 权益类型 权益名称
	public Page<Record> find(Record rd) {
		List<Object> param = new ArrayList<Object>();
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");
		// 设置sql
		StringBuilder sql1 = new StringBuilder(
				" select s.serviceId,s.serviceName,s.startDate,s.endDate,s.serviceProvision,st.typeId,st.typeName ");
		StringBuilder sql2 = new StringBuilder(" from service s ");
		sql2.append(" LEFT JOIN servicetype st on s.typeId=st.typeId ");
		sql2.append(" where 1=1 ");
		// 有效数据
		sql2.append(" and s.valid=" + VerifyStatus.valid.code);
		String serviceName = rd.get("serviceName");
		if (StringUtil.isNotBlank(serviceName)) {
			sql2.append(" and s.serviceName like ?");
			param.add("%" + serviceName + "%");
		}
		Integer typeId = rd.get("typeId");
		if (typeId != null) {
			sql2.append(" and st.typeId = ?");
			param.add(typeId);
		}
		sql2.append(" ORDER BY s.serviceId DESC");
		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString(), param.toArray());
	}

	public List<Record> findSupplyerIdName() {
		String sql = "select si.supplyerId,concat(si.supplyerName,'  商户尾号:', right(si.merchantCode,4),'  终端尾号:',right(si.termCode,4)) supplyerName from supplyerinfo si where si.state=1";
		return Db.find(sql);
	}

	public List<Record> findServiceTypeIdName() {
		String sql = "select st.typeId,st.typeName from servicetype st";
		return Db.find(sql);
	}

	public List<Record> findCardTypeIdName() {
		String sql = "select ct.CardTypeID,CONCAT(ct.Description,'(',ct.CardType,')') Description from cardtype ct where CardTypeID !=-2";
		return Db.find(sql);
	}

	public List<Record> findSupplyerIdByServiceId(Integer serviceId) {
		String sql = "select ss.supplyerId from service_supplyerinfo ss where ss.serviceId=? and ss.valid="
				+ VerifyStatus.valid.code;
		return Db.find(sql, serviceId);
	}

	public List<Record> findRightAndRightRuleByServiceId(Integer serviceId) {
		// XXX 注意！使用group_concat 设置长度限制
		Db.update("SET group_concat_max_len = 20000");
		// 设置sql
		StringBuilder sql = new StringBuilder("SELECT ri.RightID,ri.Display,ri.NeedMoney/100 NeedMoney,ri.DailyNumTop,");
		sql.append(" ri.MonthNumTop,ri.YearNumTop,ri.SelfRule1,ri.DailyNumPerStore,");
		sql.append(" GROUP_CONCAT(ct.Description,'(',ct.CardType,')_',CAST(rc.Rank AS char),'_',CAST(rc.Count AS char),'_',vs.VDescription) rightRules");
		sql.append(" from rightinfo ri ");
		sql.append(" LEFT JOIN rightcount rc on rc.RightID=ri.RightID");
		sql.append(" LEFT JOIN cardtype ct on rc.Rank=ct.CardTypeID");
		sql.append(" LEFT JOIN verifystatus vs on rc.VID=vs.VID");
		sql.append(" where ri.ServiceID=? and rc.valid=" + VerifyStatus.valid.code + " and ri.valid="
				+ VerifyStatus.valid.code);
		sql.append(" GROUP BY ri.RightID");
		return Db.find(sql.toString(), serviceId);
	}

	// public List<Record> findRightByServiceId(Integer serviceId) {
	// StringBuilder sql=new StringBuilder("SELECT ri.RightID,ri.Display,ri.NeedMoney,ri.DailyNumTop,");
	// sql.append(" ri.SelfRule1,ri.SelfRule2,ri.DailyNumPerStore");
	// sql.append(" from rightinfo ri ");
	// sql.append(" where ri.ServiceID=? ");
	// sql.append(" and ri.valid="+VerifyStatus.valid.code);
	// return Db.find(sql.toString() , serviceId);
	// }
	// public List<Record> findRightRuleByServiceId(Integer serviceId) {
	// StringBuilder sql=new StringBuilder("SELECT ri.RightID,ri.Display,ri.NeedMoney,ri.DailyNumTop,");
	// sql.append(" ri.SelfRule1,ri.SelfRule2,ri.DailyNumPerStore");
	// sql.append(" from rightinfo ri ");
	// sql.append(" where ri.ServiceID=? ");
	// sql.append(" and ri.valid="+VerifyStatus.valid.code);
	// return Db.find(sql.toString() , serviceId);
	// }
	// 升级版本
	public boolean upSupplyerInfoVersion(String supplyerId) {
		String sql = "UPDATE supplyerinfo si set si.version=RIGHT(CONCAT('000',si.version+1),4)  where supplyerId=?";
		return 1 == Db.update(sql, supplyerId);
	}

	/**
	 * 删除权益权益，当权益规则为可删时
	 * 
	 * @param needRankList
	 * @param rightID
	 */
	public void deleteRightRuleCheckVID(List<Integer> needRankList, Integer rightID) throws EditEntityException {
		StringBuilder sql = new StringBuilder("select * from rightcount rc where rc.RightID=? ");
		if (needRankList.size() != 0) {
			sql.append(" and rc.Rank not in(");
			for (Integer rank : needRankList) {
				sql.append(rank + ",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		sql.append(" and rc.VID in(" + VerifyStatus.auditPassRightRule.code + ","
				+ VerifyStatus.stopUsingRightRule.code + "," + VerifyStatus.assignedRightRule.code + ")");
		List<Record> notDelete = Db.find(sql.toString(), rightID);
		if (notDelete.size() == 0) {
			StringBuilder delSql = new StringBuilder("delete from rightcount  where  RightID=? ");
			if (needRankList.size() != 0) {
				delSql.append(" and Rank not in(");
				for (Integer rank : needRankList) {
					delSql.append(rank + ",");
				}
				delSql.deleteCharAt(delSql.length() - 1);
				delSql.append(")");
			}
			Db.update(delSql.toString(), rightID);
		} else {
			throw new EditEntityException("权益规则的状态不能修改");
		}
	}
}

package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class RightInfoDao {

	public static RightInfoDao dao = new RightInfoDao();

	public boolean add(Record record) throws Exception {
		return Db.save("rightinfo", "RightID", record);
	}

	public boolean edit(Record record) throws Exception {
		return Db.update("rightinfo", "RightID", record);
	}

	public boolean delete(Object id) throws Exception {
		return Db.deleteById("rightinfo", "RightID", id);
	}

	// 供应方机构 权益类型 权益名称
	public Page<Record> find(Record rd) {
		List<Object> param = new ArrayList<Object>();
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");
		// 设置sql
		StringBuilder sql1 = new StringBuilder(
				" select ri.RightID,si.SupplyerID,si.SupplyerName,rt.TypeID,rt.TypeDescription,");
		sql1.append("ri.Display,ri.StartTime,ri.EndTime,rct.CountTypeID,rct.CountTypeDes,");
		sql1.append("ri.NeedPoint,ri.NeedMoney/100 NeedMoney,ri.ConsumAddress,vs.VID,vs.VDescription ");
		StringBuilder sql2 = new StringBuilder(" from rightinfo ri ");
		sql2.append(" LEFT JOIN supplyerinfo si on ri.SupplyerID=si.SupplyerID ");
		sql2.append(" LEFT JOIN righttype rt on ri.TypeID=rt.TypeID ");
		sql2.append(" LEFT JOIN rightcounttype rct on ri.CountTypeID=rct.CountTypeID ");
		sql2.append(" LEFT JOIN verifystatus vs on ri.VID=vs.VID ");
		sql2.append(" where 1=1 ");
		String SupplyerID = rd.get("SupplyerID");
		if (StringUtil.isNotBlank(SupplyerID)) {
			sql2.append(" and si.SupplyerID = ?");
			param.add(SupplyerID);
		}
		Integer TypeID = rd.get("TypeID");
		if (TypeID != null) {
			sql2.append(" and rt.TypeID = ?");
			param.add(TypeID);
		}
		String Display = rd.get("Display");
		if (StringUtil.isNotBlank(Display)) {
			sql2.append(" and ri.Display like ?");
			param.add("%" + Display + "%");
		}
		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString(), param.toArray());
	}

	public List<Record> findSupplyerIdName() {
		String sql = "select si.SupplyerId,si.SupplyerName,si.Address  from supplyerinfo si";
		return Db.find(sql);
	}

	public List<Record> findRightTypeIdName() {
		String sql = "select rt.TypeID,rt.TypeDescription from righttype rt";
		return Db.find(sql);
	}

	public List<Record> findCountTypeIdName() {
		String sql = "select rct.CountTypeID,rct.CountTypeDes from rightcounttype rct";
		return Db.find(sql);
	}

}

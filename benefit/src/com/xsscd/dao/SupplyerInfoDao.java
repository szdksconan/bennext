package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.vo.VerifyStatus;

public class SupplyerInfoDao {

	public static SupplyerInfoDao dao = new SupplyerInfoDao();

	public boolean add(Record record) throws Exception {
		return Db.save("supplyerinfo", "supplyerId", record);
	}

	public boolean edit(Record record) throws Exception {
		return Db.update("supplyerinfo", "supplyerId", record);
	}

	public boolean delete(Object id) throws Exception {
		return Db.deleteById("supplyerinfo", "supplyerId", id);
	}

	// 供应方机构 权益类型 权益名称
	public Page<Record> find(Record rd) {
		Db.update("SET group_concat_max_len = 20000");
		List<Object> param = new ArrayList<Object>();
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");

		// 设置sql
		StringBuilder sql1 = new StringBuilder(" SELECT si.supplyerId,si.supplyerName,si.contact,si.address,");
		sql1.append(" si.longitude,si.latitude,si.typeId,si.trafficDate,si.merchantCode,");
		sql1.append(" si.termCode,si.remarks,si.state,t.files ");
		StringBuilder sql2 = new StringBuilder("from supplyerinfo si ");
		sql2.append(" LEFT JOIN ( select  si.supplyerId sid,GROUP_CONCAT(fi.fileId,'_',fi.fileName,'_',fi.filePath) files ");
		sql2.append(" from  supplyerinfo si LEFT JOIN fileinfo fi on (si.supplyerId =fi.targetId and fi.targetTable='supplyerinfo')");
		sql2.append(" where 1=1 ");
		// 提高性能
		String supplyerName = rd.get("supplyerName");
		if (StringUtil.isNotBlank(supplyerName)) {
			sql2.append(" and si.supplyerName like ?");
			param.add("%" + supplyerName + "%");
		}
		sql2.append(" GROUP BY si.supplyerId )t on si.supplyerId=t.sid");
		sql2.append(" LEFT JOIN supplyertype st on si.typeId=st.TypeID");
		sql2.append(" where 1=1 ");
		if (StringUtil.isNotBlank(supplyerName)) {
			sql2.append(" and si.supplyerName like ?");
			param.add("%" + supplyerName + "%");
		}
		Integer typeId = rd.get("typeId");
		if (typeId != null) {
			sql2.append(" and si.typeId = ?");
			param.add(typeId);
		}
		sql2.append(" ORDER BY si.supplyerName,si.supplyerId DESC ");
		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString(), param.toArray());
	}

	public List<Record> findSupplyerTypeIdName() {
		String sql = "select TypeID,Description  from supplyertype ";
		return Db.find(sql);
	}

	public List<Record> findSupplyerService(Object supplyerId) {
		String sql = "SELECT * from service_supplyerinfo ss where ss.supplyerId=? and ss.valid="
				+ VerifyStatus.valid.code;
		return Db.find(sql, supplyerId);
	}

}

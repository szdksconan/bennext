package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class PublishDao {

	public static PublishDao dao = new PublishDao();

	public boolean add(Record record) throws Exception {
		return Db.save("publish", "InfoId", record);
	}

	public boolean edit(Record record) throws Exception {
		return Db.update("publish", "InfoId", record);
	}

	public boolean delete(Object id) throws Exception {
		return Db.deleteById("publish", "InfoId", id);
	}

	// 供应方机构 权益类型 权益名称
	public Page<Record> find(Record rd) {
		// XXX 注意！使用group_concat 设置长度限制
		Db.update("SET group_concat_max_len = 20000");
		List<Object> param = new ArrayList<Object>();
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" SELECT p.InfoId,p.Title,pc.ClassID,pc.ClassDescription,");
		sql1.append(" p.PublishTime,p.Abstract,p.Content,t.files");
		StringBuilder sql2 = new StringBuilder(" from publish p");
		sql2.append(" LEFT JOIN (");
		sql2.append(" select  p.InfoId pid,GROUP_CONCAT(fi.fileId,'_',fi.fileName,'_',fi.filePath) files");
		sql2.append(" from  publish p LEFT JOIN fileinfo fi on (p.InfoId =fi.targetId and fi.targetTable='publish')");
		sql2.append(" where 1=1");
		// 提高性能
		String Title = rd.get("Title");
		if (StringUtil.isNotBlank(Title)) {
			sql2.append(" and p.Title like ? ");
			param.add("%" + Title + "%");
		}
		sql2.append(" GROUP BY p.InfoId");
		sql2.append(" )t on p.InfoId=t.pid");
		sql2.append(" LEFT JOIN publishclass pc on p.ClassID=pc.ClassID");
		sql2.append(" where 1=1 ");
		if (StringUtil.isNotBlank(Title)) {
			sql2.append(" and p.Title like ? ");
			param.add("%" + Title + "%");
		}
		Integer ClassID = rd.get("ClassID");
		if (ClassID != null) {
			sql2.append(" and p.ClassID=?");
			param.add(ClassID);
		}
		sql2.append(" ORDER BY p.InfoId DESC ");
		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString(), param.toArray());
	}

	public List<Record> findPublishClassIdName() {
		String sql = "select ClassID,ClassDescription  from publishclass";
		return Db.find(sql);
	}

	public Page<Record> findPublishFront(Record rd) {
		// XXX 注意！使用group_concat 设置长度限制
		Db.update("SET group_concat_max_len = 20000");
		List<Object> param = new ArrayList<Object>();
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" SELECT p.InfoId,p.Title,p.Abstract,t.files");
		StringBuilder sql2 = new StringBuilder(" from publish p");
		sql2.append(" LEFT JOIN (");
		sql2.append(" select  p.InfoId pid,GROUP_CONCAT(fi.filePath) files");
		sql2.append(" from  publish p LEFT JOIN fileinfo fi on (p.InfoId =fi.targetId and fi.targetTable='publish')");
		sql2.append(" where 1=1");
		// 提高性能
		String Title = rd.get("Title");
		if (StringUtil.isNotBlank(Title)) {
			sql2.append(" and p.Title like ? ");
			param.add("%" + Title + "%");
		}
		sql2.append(" GROUP BY p.InfoId");
		sql2.append(" )t on p.InfoId=t.pid");
		sql2.append(" LEFT JOIN publishclass pc on p.ClassID=pc.ClassID");
		sql2.append(" where 1=1 ");
		if (StringUtil.isNotBlank(Title)) {
			sql2.append(" and p.Title like ? ");
			param.add("%" + Title + "%");
		}
		Integer ClassID = rd.get("ClassID");
		if (ClassID != null) {
			sql2.append(" and p.ClassID=?");
			param.add(ClassID);
		}
		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString(), param.toArray());
	}

	public Record findPublishDetailFront(Integer infoId) {
		// XXX 注意！使用group_concat 设置长度限制
		Db.update("SET group_concat_max_len = 20000");
		StringBuilder sql = new StringBuilder(" select  p.InfoId pid,p.Title,pc.ClassID,pc.ClassDescription,");
		sql.append(" p.PublishTime,p.Abstract,p.Content,");
		sql.append(" GROUP_CONCAT(fi.filePath) files ");
		sql.append(" from  publish p ");
		sql.append(" LEFT JOIN fileinfo fi on (p.InfoId =fi.targetId and fi.targetTable='publish')");
		sql.append(" LEFT JOIN publishclass pc on p.ClassID=pc.ClassID");
		sql.append(" where p.InfoId=?");
		sql.append(" GROUP BY p.InfoId");
		return Db.findFirst(sql.toString(), infoId);
	}

}

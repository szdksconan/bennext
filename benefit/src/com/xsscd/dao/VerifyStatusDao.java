package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 状态信息相关数据库访问
 * @author zengcy
 *
 */
public class VerifyStatusDao {
	public static VerifyStatusDao dao = new VerifyStatusDao();
	
	
	public List<Record> findBackUserVerifyStatusIdName() {
		String sql="select vs.VID,vs.VDescription from verifystatus vs where vs.VID like '10%'";
		return Db.find(sql);
	}

}

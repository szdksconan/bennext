package com.xsscd.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class UsingRecordDao {
	public boolean insert(Record record){
		record.set("State", 1);
		return Db.save("usingrecord", record);
	}
	public boolean delete(Record record){
//		String sql = "delete from usingrecord where LocalTransDate="+record.getStr("LocalTransDate")+" and LocalTransTime="+record.getStr("LocalTransTime");
		String sql = "update usingrecord set State = 9 where TerminalNum = "+record.getStr("TerminalNum")
				+ " and LocalTransDate=? and LocalTransTime=?";
		return 1==Db.update(sql,record.getStr("LocalTransDate"),record.getStr("LocalTransTime"));
	}
}

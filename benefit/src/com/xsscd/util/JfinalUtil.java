package com.xsscd.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class JfinalUtil {
	/**
	 * 把 list<Record> 的null改为空字符串
	 */
	public static void convertNullToNullString(List<Record> list) {
		for (Record rd : list) {
			convertNullToNullString(rd);
		}
	}

	public static void convertNullToNullString(Record rd) {
		for (java.util.Iterator<Entry<String, Object>> it = rd.getColumns().entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			if (e.getValue() == null) {
				e.setValue("");
			}
		}
	}

	/**
	 * 是否重复
	 * 
	 * @return
	 */
	public static Record findRecord(String tableName, Map<String, Object> columnValues) {
		StringBuilder sql = new StringBuilder("select * from " + tableName + " t where 1=1");
		List<Object> paras = new ArrayList<Object>();
		for (String key : columnValues.keySet()) {
			sql.append(" and t." + key + "=?");
			paras.add(columnValues.get(key));
		}
		return Db.findFirst(sql.toString(), paras.toArray());
	}
}

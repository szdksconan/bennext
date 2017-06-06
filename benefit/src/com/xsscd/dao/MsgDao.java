package com.xsscd.dao;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;

public class MsgDao {
	public static MsgDao dao = new MsgDao();

	public boolean addMsg(String phone, String content, String msgType) {
		String sql = "INSERT INTO msgrecord(phone,content,sendDate,msgType) VALUES(?,?,?,?)";
		return 1 == Db.update(sql, phone, content, new Date(), msgType);
	}
}

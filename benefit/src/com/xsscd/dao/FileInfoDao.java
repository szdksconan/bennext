package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class FileInfoDao {

	public static FileInfoDao dao =new FileInfoDao();

	public  boolean addFileInfo(String fileId, String filePath,
			String fileName) {
		String sql="INSERT INTO fileinfo(fileId,filePath,fileName) VALUES(?,?,?)";
		return 1==Db.update(sql, fileId,filePath,fileName);
	}

	public String findFileName(String fileId) {
		String sql="select fileName,fileId from fileinfo where fileId=?";
		Record rd= Db.findFirst(sql, fileId);
		return (rd==null?"":rd.getStr("fileName"));
	}

	public boolean deleteFileInfo(String fileId) {
		return Db.deleteById("fileinfo", "fileId", fileId);
	}

	public boolean  editFileTarget(String fileId, Integer supplyerId,
			String targetTable) {
		Record rd =new Record();
		rd.set("fileId", fileId);
		rd.set("targetId", supplyerId);
		rd.set("targetTable", targetTable);
		return Db.update("fileinfo", "fileId", rd);
	}

	public List<Record> findTargetFiles(Integer supplyerId, String targetTable) {
		String sql="select fi.fileId,fi.fileName from fileinfo fi where fi.targetId=? and fi.targetTable=?";
		return Db.find(sql, supplyerId,targetTable);
	}

}

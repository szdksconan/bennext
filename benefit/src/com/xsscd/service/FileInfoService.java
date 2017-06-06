package com.xsscd.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.xsscd.base.BaseService;
import com.xsscd.controller.FileInfoController;
import com.xsscd.dao.FileInfoDao;
import com.xsscd.vo.ResultVo;

public class FileInfoService extends BaseService {

	public static FileInfoService service = new FileInfoService();

	// TODO 文件操作的分隔符/用 File.separator
	public void uploadFile(Controller ct) throws Exception {
		String baseFilePath = ct.getSession().getServletContext().getRealPath(File.separator + "files");
		// baseFilePath.replaceAll("\\\\", "\\\\\\\\");
		// 存储文件
		UploadFile file = ct.getFile("uploadfile", baseFilePath);
		try {
			String filePath = "files/" + file.getFileName();
			String fileName = file.getFileName();
			// BaseService.log.info(baseFilePath + "--" + filePath + "--" + file + "--" + file.getSaveDirectory() +
			// "--");
			String fileId = UUID.randomUUID().toString();
			// 存入数据
			if (!FileInfoDao.dao.addFileInfo(fileId, filePath, fileName)) {
				throw new Exception();
			}
			;
			// 返回正确数据
			ResultVo rv = new ResultVo(true, "上传文件成功");
			List<Record> list = new ArrayList<Record>();
			Record rd = new Record();
			rd.set("fileId", fileId);
			rd.set("fileName", fileName);
			rd.set("filePath", filePath);
			list.add(rd);
			rv.setItemList(list);
			ct.renderJson(rv);
		} catch (Exception e) {
			if (file != null) {
				this.deleteFile(baseFilePath + File.separator + file.getFileName());
			}
			throw e;
		}

	}

	// public static void main(String[] args) {
	// String s = "D:\\java\\src\\myjava";
	// String s1 = "D:" + File.separator + "java" + File.separator + "src" + File.separator + "myjava";
	// System.out.println(s + "--" + s1);
	//
	// }

	// 删除表数据
	@Override
	public boolean delete(Object id, Controller ct) throws Exception {
		String fileId = (String) id;
		// 查询文件路径
		String fileName = FileInfoDao.dao.findFileName(fileId);
		// 删除数据
		if (!FileInfoDao.dao.deleteFileInfo(fileId)) {
			throw new Exception("删除失败");
		}
		;
		if (StringUtils.isNotBlank(fileName)) {
			((FileInfoController) ct).getDelFileList().add(fileName);
		}
		return true;
	}

	@Override
	public void deleteBatchTransactionResult(Controller ct, ResultVo rv) {
		// 事务成功则删除文件
		if (rv.getIsSuccess()) {
			FileInfoController fc = (FileInfoController) ct;
			if (fc.getDelFileList() != null) {
				FileInfoService.service.deleteFilesByList(ct, fc.getDelFileList());
			}
		}
	}

	// 删除list中所有表数据
	public void deleteFileTableDatasByList(List<Record> noNeedFiles) throws Exception {
		for (Record record : noNeedFiles) {
			String fileId = record.getStr("fileId");
			if (!FileInfoDao.dao.deleteFileInfo(fileId))
				throw new Exception("删除文件表信息遇到问题");
		}
	}

	// !!!请事务完成后使用 删除list中所有文件
	public void deleteFilesByList(Controller ct, List<String> noNeedFiles) {
		// 删除不需要的文件
		String baseFilePath = ct.getSession().getServletContext().getRealPath(File.separator + "files");
		for (String fileName : noNeedFiles) {
			this.deleteFile(baseFilePath + File.separator + fileName);
		}
	}
}

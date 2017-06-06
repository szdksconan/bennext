package com.xsscd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.controller.PublishController;
import com.xsscd.dao.FileInfoDao;
import com.xsscd.dao.PublishDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.util.DateUtil;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;

public class PublishService extends BaseService {

	public static PublishService service = new PublishService();

	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("Title", ct.getPara("Title"));
		rd.set("ClassID", ct.getParaToInt("ClassID"));
		rd.set("PublishTime", DateUtil.formatDateTime(ct.getPara("PublishTime")));
		rd.set("Abstract", ct.getPara("Abstract"));
		rd.set("Content", ct.getPara("Content"));
		// 重复信息标题
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("Title", rd.get("Title"));
		if (JfinalUtil.findRecord("publish", cv) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的信息标题的存在重复");
			throw ae;
		}
		if (!PublishDao.dao.add(rd))
			throw new Exception("添加发布信息遇到问题");
		Record saveRd = JfinalUtil.findRecord("publish", cv);
		Integer InfoId = saveRd.getInt("InfoId");
		// 关联所有商户文件,用逗号分割
		String fileIds = ct.getPara("fileIds");
		if (StringUtils.isNotBlank(fileIds)) {
			String[] idArr = fileIds.split(",");
			for (String fileId : idArr) {
				// 修改file target
				if (!FileInfoDao.dao.editFileTarget(fileId, InfoId, "publish"))
					throw new Exception("修改filetarget遇到问题");
			}
		}
		return true;

	}

	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("InfoId", ct.getParaToInt("InfoId"));
		rd.set("Title", ct.getPara("Title"));
		rd.set("ClassID", ct.getParaToInt("ClassID"));
		rd.set("PublishTime", DateUtil.formatDateTime(ct.getPara("PublishTime")));
		rd.set("Abstract", ct.getPara("Abstract"));
		rd.set("Content", ct.getPara("Content"));
		// 重复信息标题
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("Title", rd.get("Title"));
		Record oldRd = JfinalUtil.findRecord("publish", cv);
		if (oldRd != null && !oldRd.get("InfoId").equals(ct.getParaToInt("InfoId"))) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("errorInfo", "修改的信息标题的存在重复");
			throw ee;
		}
		if (!PublishDao.dao.edit(rd))
			throw new Exception("修改发布信息遇到问题");
		Integer InfoId = ct.getParaToInt("InfoId");
		// 关联所有商户文件,用逗号分割
		String fileIds = ct.getPara("fileIds");
		if (StringUtils.isNotBlank(fileIds)) {
			String[] idArr = fileIds.split(",");
			List<Record> noNeedFiles = FileInfoDao.dao.findTargetFiles(InfoId, "publish");
			Map<String, String> map = new HashMap<String, String>();
			// 转换数据结构
			for (Record noNeedFile : noNeedFiles) {
				map.put(noNeedFile.getStr("fileId"), noNeedFile.getStr("fileName"));
			}
			for (String fileId : idArr) {
				// 修改file target
				if (!FileInfoDao.dao.editFileTarget(fileId, InfoId, "publish"))
					throw new Exception("修改filetarget遇到问题");
				// 需要的留下
				map.remove(fileId);
			}
			// 删除不需要的表数据
			for (String fileId : map.keySet()) {
				if (!FileInfoDao.dao.deleteFileInfo(fileId))
					throw new Exception("删除文件表数据出现问题");
			}
			// 删除不需要的文件
			String baseFilePath = ct.getSession().getServletContext().getRealPath("/files");
			for (String fileName : map.values()) {
				this.deleteFile(baseFilePath + "/" + fileName);
			}
		}
		return true;

	}

	@Override
	public boolean delete(Object InfoId, Controller ct) throws Exception {
		if (!PublishDao.dao.delete(InfoId))
			throw new Exception("删除发布信息遇到问题");
		List<Record> noNeedFiles = FileInfoDao.dao.findTargetFiles(Integer.valueOf((String) InfoId), "publish");
		// 删除file表数据
		FileInfoService.service.deleteFileTableDatasByList(noNeedFiles);
		// 把要删除的file文件放入数组中
		PublishController pc = (PublishController) ct;
		for (Record record : noNeedFiles) {
			String fileName = record.getStr("fileName");
			pc.getDelFileList().add(fileName);
		}
		return true;
	}

	@Override
	public void deleteBatchTransactionResult(Controller ct, ResultVo rv) {
		// 事务成功则删除文件
		if (rv.getIsSuccess()) {
			PublishController pc = (PublishController) ct;
			if (pc.getDelFileList() != null) {
				FileInfoService.service.deleteFilesByList(ct, pc.getDelFileList());
			}
		}
	}

	public void find(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("ClassID", ct.getParaToInt("ClassID"));
		rd.set("Title", URLUtil.decodeUTF8(ct.getPara("Title")));
		Page<Record> page = PublishDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		// 转换数据结构
		for (Record record : page.getList()) {
			String files = record.getStr("files");
			List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
			if (StringUtils.isNotBlank(files)) {
				String[] fileArr = files.split(",");
				for (String fileinfo : fileArr) {
					Map<String, String> map = new HashMap<String, String>();
					String[] file = fileinfo.split("_");
					String fileId = file[0], fileName = file[1], filePath = file[2];
					map.put("fileId", fileId);
					map.put("fileName", fileName);
					map.put("filePath", filePath);
					fileList.add(map);
				}

			}
			record.remove("files");
			record.set("fileList", fileList);
		}
		ResultVo rv = new ResultVo(true, "查询发布信息数据成功", page.getList(), page.getTotalRow());
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));
	}

	public void findPublishClassIdName(Controller ct) {
		List<Record> list = PublishDao.dao.findPublishClassIdName();
		JfinalUtil.convertNullToNullString(list);
		ResultVo rv = new ResultVo(true, "查询发布信息类别数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findPublishFront(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("ClassID", ct.getParaToInt("ClassID"));
		rd.set("Title", URLUtil.decodeUTF8(ct.getPara("Title")));
		Page<Record> page = PublishDao.dao.findPublishFront(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询前台发布信息数据成功", page.getList(), page.getTotalRow());
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));
	}

	public void findPublishDetailFront(Controller ct) {
		Integer InfoId = ct.getParaToInt("InfoId");
		Record rd = PublishDao.dao.findPublishDetailFront(InfoId);
		List<Record> list = new ArrayList<Record>();
		list.add(rd);
		ResultVo rv = new ResultVo(true, "查询前台发布信息详情数据成功", list, 1);
		if (rd == null) {
			rv.setIsSuccess(false);
			rv.setMessage("没有该发布信息的详情信息");
			rv.setCount(0);
		}
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));
	}

	public static void main(String[] args) {
		String s = "8373d117-c6fc-4691-8086-e3c3d54daea9_QQ截图20150125155611.jpg_files/QQ截图20150125155611.jpg,11_213131_files/QQ截图20150119101503.png,b7f5da3e-fab7-4e16-b61b-dec261f0e30d_a375695b2777e06b2d8b12b7dba14d3d.jpg_files/a375695b2777e06b2d8b12b7dba14d3d.jpg,7_4324_files/天空.png,b7a8a15a-9e6c-421e-9453-3a0d2ecca964_QQ截图20150125155646.jpg_files/QQ截图20150125155646.jpg,12_2313_files/QQ截图20150119101531.png,de671dfb-3b06-411a-98c5-5c67f1146752_QQ截图20150125155808.jpg_files/QQ截图20150125155808.jpg";
		System.out.println("PublishService.main()" + s.length());
	}
}

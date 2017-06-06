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
import com.xsscd.controller.SupplyerInfoController;
import com.xsscd.dao.FileInfoDao;
import com.xsscd.dao.SupplyerInfoDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.DeleteEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;

public class SupplyerInfoService extends BaseService {

	public static SupplyerInfoService service = new SupplyerInfoService();

	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("supplyerName", ct.getPara("supplyerName"));
		rd.set("contact", ct.getPara("contact"));
		rd.set("address", ct.getPara("address"));
		rd.set("longitude", ct.getPara("longitude"));
		rd.set("latitude", ct.getPara("latitude"));
		rd.set("typeId", ct.getParaToInt("typeId"));
		rd.set("trafficDate", ct.getPara("trafficDate"));
		rd.set("merchantCode", ct.getPara("merchantCode"));
		rd.set("termCode", ct.getPara("termCode"));
		rd.set("remarks", ct.getPara("remarks"));
		rd.set("state", ct.getParaToInt("state"));
		// 商户有新的服务对版本进行控制
		rd.set("version", "0001");
		// 是否重复
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("merchantCode", ct.getPara("merchantCode"));
		cv.put("termCode", ct.getPara("termCode"));
		cv.put("state", ct.getParaToInt("state"));
		if (JfinalUtil.findRecord("supplyerInfo", cv) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加商户信息时商户代码和终端代码和终端状态联合不能重复");
			throw ae;
		}
		if (!SupplyerInfoDao.dao.add(rd))
			throw new Exception("添加商户信息遇到问题");
		Record saveRd = JfinalUtil.findRecord("supplyerInfo", cv);
		Integer supplyerId = saveRd.getInt("supplyerId");
		// 关联所有商户文件,用逗号分割
		String fileIds = ct.getPara("fileIds");
		if (StringUtils.isNotBlank(fileIds)) {
			String[] idArr = fileIds.split(",");
			for (String fileId : idArr) {
				// 修改file target
				if (!FileInfoDao.dao.editFileTarget(fileId, supplyerId, "supplyerInfo"))
					throw new Exception("修改filetarget遇到问题");
			}
		}
		return true;
	}

	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();

		rd.set("supplyerId", ct.getParaToInt("supplyerId"));
		rd.set("supplyerName", ct.getPara("supplyerName"));
		rd.set("contact", ct.getPara("contact"));
		rd.set("address", ct.getPara("address"));
		rd.set("longitude", ct.getPara("longitude"));
		rd.set("latitude", ct.getPara("latitude"));
		rd.set("typeId", ct.getParaToInt("typeId"));
		rd.set("trafficDate", ct.getPara("trafficDate"));
		rd.set("merchantCode", ct.getPara("merchantCode"));
		rd.set("termCode", ct.getPara("termCode"));
		rd.set("remarks", ct.getPara("remarks"));
		rd.set("state", ct.getParaToInt("state"));
		// 如果商户在提供服务，则不能停用
		if (rd.get("state").equals(0)) {
			List<Record> list = SupplyerInfoDao.dao.findSupplyerService(rd.get("supplyerId"));
			if (list.size() > 0)
				throw new DeleteEntityException("商户正在提供权益服务不能停用");
		}
		// 是否重复
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("merchantCode", ct.getPara("merchantCode"));
		cv.put("termCode", ct.getPara("termCode"));
		cv.put("state", ct.getParaToInt("state"));
		Record cf = JfinalUtil.findRecord("supplyerInfo", cv);
		if (cf != null && !cf.getInt("supplyerId").equals(ct.getParaToInt("supplyerId"))) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("errorInfo", "修改商户信息时商户代码和终端代码和终端状态联合不能重复");
			throw ee;
		}
		if (!SupplyerInfoDao.dao.edit(rd))
			throw new Exception("修改商户信息遇到问题");
		Integer supplyerId = ct.getParaToInt("supplyerId");
		// 关联所有商户文件,用逗号分割
		String fileIds = ct.getPara("fileIds");
		if (StringUtils.isNotBlank(fileIds)) {
			String[] idArr = fileIds.split(",");
			List<Record> noNeedFiles = FileInfoDao.dao.findTargetFiles(supplyerId, "supplyerInfo");
			Map<String, String> map = new HashMap<String, String>();
			// 转换数据结构
			for (Record noNeedFile : noNeedFiles) {
				map.put(noNeedFile.getStr("fileId"), noNeedFile.getStr("fileName"));
			}
			for (String fileId : idArr) {
				// 修改file target
				if (!FileInfoDao.dao.editFileTarget(fileId, supplyerId, "supplyerInfo"))
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
	public boolean delete(Object supplyerId, Controller ct) throws Exception {
		// 如果商户在提供服务，则不能删除
		List<Record> list = SupplyerInfoDao.dao.findSupplyerService(supplyerId);
		if (list.size() > 0) {
			throw new DeleteEntityException("商户正在提供权益服务不能删除");
		}
		if (!SupplyerInfoDao.dao.delete(supplyerId))
			throw new Exception("删除商户信息遇到问题");
		List<Record> noNeedFiles = FileInfoDao.dao
				.findTargetFiles(Integer.valueOf((String) supplyerId), "supplyerInfo");
		// 删除表数据
		FileInfoService.service.deleteFileTableDatasByList(noNeedFiles);
		// 把要删除的file文件放入数组中
		SupplyerInfoController sc = (SupplyerInfoController) ct;
		for (Record record : noNeedFiles) {
			String fileName = record.getStr("fileName");
			sc.getDelFileList().add(fileName);
		}
		return true;
	}

	@Override
	public void deleteBatchTransactionResult(Controller ct, ResultVo rv) {
		// 事务成功则删除文件
		if (rv.getIsSuccess()) {
			SupplyerInfoController sc = (SupplyerInfoController) ct;
			if (sc.getDelFileList() != null) {
				FileInfoService.service.deleteFilesByList(ct, sc.getDelFileList());
			}
		}
	}

	public void find(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("typeId", ct.getParaToInt("typeId"));

		rd.set("supplyerName", URLUtil.decodeUTF8(ct.getPara("supplyerName")));
		Page<Record> page = SupplyerInfoDao.dao.find(rd);
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
		ResultVo rv = new ResultVo(true, "查询商户数据成功", page.getList(), page.getTotalRow());
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));
	}

	public void findSupplyerTypeIdName(Controller ct) {
		List<Record> list = SupplyerInfoDao.dao.findSupplyerTypeIdName();
		ResultVo rv = new ResultVo(true, "查询商户类别数据成功", list, list.size());
		ct.renderJson(rv);
	}
}

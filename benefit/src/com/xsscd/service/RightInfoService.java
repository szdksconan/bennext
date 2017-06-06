package com.xsscd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.RightInfoDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.util.DateUtil;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;

public class RightInfoService extends BaseService {

	public static RightInfoService service = new RightInfoService();

	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("SupplyerID", ct.getPara("SupplyerID"));
		rd.set("TypeID", ct.getParaToInt("TypeID"));
		rd.set("Display", ct.getPara("Display"));
		rd.set("StartTime", DateUtil.formatDateTime(ct.getPara("StartTime")));
		rd.set("EndTime", DateUtil.formatDateTime(ct.getPara("EndTime")));
		rd.set("CountTypeID", ct.getPara("CountTypeID"));
		rd.set("NeedPoint", ct.getPara("NeedPoint"));
		rd.set("NeedMoney", ct.getPara("NeedMoney"));
		rd.set("ConsumAddress", ct.getPara("ConsumAddress"));
		rd.set("NeedPoint", ct.getPara("NeedPoint"));
		// 未审核
		rd.set("VID", 100);
		// 重复权益
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("SupplyerID", rd.get("SupplyerID"));
		cv.put("Display", rd.get("Display"));
		if (JfinalUtil.findRecord("rightinfo", cv) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的商户号、权益的组合存在重复");
			throw ae;
		}
		return RightInfoDao.dao.add(rd);
	}

	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("RightID", ct.getPara("RightID"));
		rd.set("SupplyerID", ct.getPara("SupplyerID"));
		rd.set("TypeID", ct.getParaToInt("TypeID"));
		rd.set("Display", ct.getPara("Display"));
		rd.set("StartTime", DateUtil.formatDateTime(ct.getPara("StartTime")));
		rd.set("EndTime", DateUtil.formatDateTime(ct.getPara("EndTime")));
		rd.set("CountTypeID", ct.getPara("CountTypeID"));
		rd.set("NeedMoney", ct.getPara("NeedMoney"));
		rd.set("ConsumAddress", ct.getPara("ConsumAddress"));
		rd.set("NeedPoint", ct.getPara("NeedPoint"));
		// 重复权益
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("SupplyerID", rd.get("SupplyerID"));
		cv.put("Display", rd.get("Display"));
		Record oldRd = JfinalUtil.findRecord("rightinfo", cv);
		// 未审核 未通过才可以修改
		if (oldRd != null && oldRd.getInt("VID") != null
				&& (!oldRd.getInt("VID").equals(100) && !oldRd.getInt("VID").equals(102))) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("editInfo", "未审核或未通过的权益才可以修改");
			throw ee;
		}
		if (oldRd != null && !rd.get("RightID").equals(oldRd.get("RightID") + "")) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("errorInfo", "修改的商户号、权益的组合存在重复");
			throw ee;
		}
		return RightInfoDao.dao.edit(rd);
	}

	@Override
	public boolean delete(Object id, Controller ct) throws Exception {
		return RightInfoDao.dao.delete(id);
	}

	/**
	 * 格式 id_state
	 */
	@Override
	public boolean audit(String idState, Controller ct) throws Exception {
		String[] is = idState.split("_");
		Record rd = new Record();
		rd.set("RightID", is[0]);
		rd.set("VID", is[1]);
		return RightInfoDao.dao.edit(rd);
	}

	public void find(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("SupplyerID", ct.getPara("SupplyerID"));
		rd.set("TypeID", ct.getParaToInt("TypeID"));
		rd.set("Display", URLUtil.decodeUTF8(ct.getPara("Display")));
		Page<Record> page = RightInfoDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询权益数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void findSupplyerIdName(Controller ct) {
		List<Record> list = RightInfoDao.dao.findSupplyerIdName();
		ResultVo rv = new ResultVo(true, "查询商户数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findRightTypeIdName(Controller ct) {
		List<Record> list = RightInfoDao.dao.findRightTypeIdName();
		ResultVo rv = new ResultVo(true, "查询权益类型数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findCountTypeIdName(Controller ct) {
		List<Record> list = RightInfoDao.dao.findCountTypeIdName();
		ResultVo rv = new ResultVo(true, "查询数量种类数据成功", list, list.size());
		ct.renderJson(rv);
	}

}

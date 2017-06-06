package com.xsscd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.RightRuleDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.exception.OperateEntityException;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;
import com.xsscd.vo.VerifyStatus;

public class RightRuleService extends BaseService {

	public static RightRuleService service = new RightRuleService();

	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("RightID", ct.getPara("RightID"));
		rd.set("BankID", ct.getPara("BankID"));
		rd.set("CardTypeID", ct.getPara("CardTypeID"));
		rd.set("Count", ct.getPara("Count"));
		// 未审核
		rd.set("VID", 110);
		// 重复的权益规则
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("RightID", rd.get("RightID"));
		cv.put("BankID", rd.get("BankID"));
		cv.put("CardTypeID", rd.get("CardTypeID"));
		if (JfinalUtil.findRecord("rightrule", cv) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的权益、银行、卡类型的组合存在重复");
			throw ae;
		}
		return RightRuleDao.dao.add(rd);
	}

	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("RuleID", ct.getPara("RuleID"));
		rd.set("RightID", ct.getPara("RightID"));
		rd.set("BankID", ct.getPara("BankID"));
		rd.set("CardTypeID", ct.getPara("CardTypeID"));
		rd.set("Count", ct.getPara("Count"));
		// 重复权益
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("RightID", rd.get("RightID"));
		cv.put("BankID", rd.get("BankID"));
		cv.put("CardTypeID", rd.get("CardTypeID"));
		Record oldRd = JfinalUtil.findRecord("rightrule", cv);
		// 未审核 未通过才可以修改
		if (oldRd != null && oldRd.getInt("VID") != null
				&& (!oldRd.getInt("VID").equals(110) && !oldRd.getInt("VID").equals(112))) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("editInfo", "未审核或未通过的权益规则才可以修改");
			throw ee;
		}
		if (oldRd != null && !rd.get("RuleID").equals(oldRd.get("RuleID") + "")) {
			EditEntityException ee = new EditEntityException();
			ee.getMap().put("editInfo", "修改的权益、银行、卡类型的组合存在重复");
			throw ee;
		}
		return RightRuleDao.dao.edit(rd);
	}

	@Override
	public boolean delete(Object id, Controller ct) throws Exception {
		return RightRuleDao.dao.delete(id);
	}

	/**
	 * 分配权益规则
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void allocate(String id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("RuleID", id);
		Record oldRd = JfinalUtil.findRecord("rightrule", param);
		if (!oldRd.get("VID").equals(111)) {
			OperateEntityException oe = new OperateEntityException();
			oe.getMap().put("errorInfo", "当前权益规则的状态不能被分配");
			throw oe;
		}
		Record rd = new Record();
		rd.set("RuleID", id);
		rd.set("VID", 113);
		boolean isSuccess = RightRuleDao.dao.edit(rd);
		if (!isSuccess) {
			throw new Exception();
		}
	}

	/**
	 * 审核数据 格式 id_state
	 */
	// @Override
	// public boolean audit(String idState,Controller ct) throws Exception {
	// String[] is=idState.split("_");
	// Record rd=new Record();
	// rd.set("RuleID", is[0]);
	// rd.set("VID", is[1]);
	// return RightRuleDao.dao.edit(rd);
	// }

	public void findLink(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("rightName", URLUtil.decodeUTF8(ct.getPara("rightName")));
		rd.set("bankName", URLUtil.decodeUTF8(ct.getPara("bankName")));
		rd.set("cardTypeName", URLUtil.decodeUTF8(ct.getPara("cardTypeName")));
		rd.set("vids", "110,111,112");
		Page<Record> page = RightRuleDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询权益规则关联数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void findAllocation(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("rightName", URLUtil.decodeUTF8(ct.getPara("rightName")));
		rd.set("bankName", URLUtil.decodeUTF8(ct.getPara("bankName")));
		rd.set("cardTypeName", URLUtil.decodeUTF8(ct.getPara("cardTypeName")));
		rd.set("vids", "111,113");
		Page<Record> page = RightRuleDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询权益规则分配数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void findAuditPassRightIdName(Controller ct) {
		List<Record> list = RightRuleDao.dao.findAuditPassRightIdName();
		ResultVo rv = new ResultVo(true, "查询通过的权益数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findBankIdName(Controller ct) {
		List<Record> list = RightRuleDao.dao.findBankIdName();
		ResultVo rv = new ResultVo(true, "查询银行数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findCardTypeIdName(Controller ct) {
		String BankID = ct.getPara("BankID");
		List<Record> list = RightRuleDao.dao.findCardTypeIdName(BankID);
		ResultVo rv = new ResultVo(true, "查询卡类型数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * -------------------------------------新版本----------------------------------------------------
	 */
	public void find(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("rightName", URLUtil.decodeUTF8(ct.getPara("rightName")));
		rd.set("serviceName", URLUtil.decodeUTF8(ct.getPara("serviceName")));
		Page<Record> page = RightRuleDao.dao.findRightRule(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询权益规则数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public boolean auditRightRule(String idState) throws Exception {
		String[] is = idState.split("_");
		Integer RightID = Integer.valueOf(is[0]), Rank = Integer.valueOf(is[1]), VID = Integer.valueOf(is[2]);
		// 检测当前状态是否可以完成当前操作
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("RightID", RightID);
		cv.put("Rank", Rank);
		cv.put("valid", VerifyStatus.valid.code);
		Record rd = JfinalUtil.findRecord("rightcount", cv);
		if (rd != null) {
			Integer oldVID = rd.getInt("VID");
			// 请求未通过
			if (VID.equals(VerifyStatus.auditNoPassRightRule.code)) {
				if (!oldVID.equals(VerifyStatus.noAuditRightRule.code)) {
					throw new OperateEntityException("当前状态不是" + VerifyStatus.noAuditRightRule.description + ",不能审核未通过");
				}
			} else
			// 请求通过
			if (VID.equals(VerifyStatus.auditPassRightRule.code)) {
				if (!(oldVID.equals(VerifyStatus.noAuditRightRule.code) || oldVID
						.equals(VerifyStatus.auditNoPassRightRule.code))) {
					throw new OperateEntityException("当前状态不是" + VerifyStatus.noAuditRightRule.description + "或"
							+ VerifyStatus.auditNoPassRightRule.description + ",不能审核通过");
				}
				// 临时表加入通过数据
				if (!RightRuleDao.dao.copyRightCountForTemp(RightID, Rank))
					new OperateEntityException("添加权益规则的临时数据出错");
			} else
			// 请求停用
			if (VID.equals(VerifyStatus.stopUsingRightRule.code)) {
				if (!oldVID.equals(VerifyStatus.assignedRightRule.code)) {
					throw new OperateEntityException("当前状态不是" + VerifyStatus.assignedRightRule.description + ",不能审核停用");
				}
				// 临时表加入停用数据
				if (!RightRuleDao.dao.copyRightCountForTemp(RightID, Rank))
					new OperateEntityException("添加权益规则的临时数据出错");
			} else {
				throw new OperateEntityException("不明白的审核操作!");
			}
		} else {
			throw new OperateEntityException("审核的权益规则数据不存在");
		}
		return RightRuleDao.dao.audit(VID, RightID, Rank);
	}
}

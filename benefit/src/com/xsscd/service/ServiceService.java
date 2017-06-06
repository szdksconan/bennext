package com.xsscd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.ServiceDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.DeleteEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.util.DateUtil;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;
import com.xsscd.vo.VerifyStatus;

public class ServiceService extends BaseService {

	public static ServiceService service = new ServiceService();

	public static void main(String[] args) {
		System.out.println("ServiceService.main()" + 1.3333 * 100);
	}

	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		// 服务基本信息
		rd.set("typeId", ct.getParaToInt("typeId"));
		rd.set("serviceName", ct.getPara("serviceName"));
		rd.set("startDate", DateUtil.formatDateTime(ct.getPara("startDate")));
		rd.set("endDate", DateUtil.formatDateTime(ct.getPara("endDate")));
		rd.set("serviceProvision", ct.getPara("serviceProvision"));
		rd.set("valid", VerifyStatus.valid.code);
		// 重复服务
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("serviceName", rd.get("serviceName"));
		if (JfinalUtil.findRecord("service", cv) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的服务名称信息存在重复");
			throw ae;
		}
		if (!ServiceDao.dao.add(rd))
			return false;
		Integer serviceId = JfinalUtil.findRecord("service", cv).getInt("serviceId");
		// 多个商户关联 supplyerIds 1,3,54,5
		String supplyerIds = ct.getPara("supplyerIds");
		String[] supplyerIdArr = supplyerIds.split(",");
		if (supplyerIdArr != null) {
			for (String supplyerId : supplyerIdArr) {
				// 给商户添加新服务
				if (!ServiceDao.dao.addServiceSupplyerInfo(serviceId, Integer.valueOf(supplyerId)))
					return false;
				// 商户版本升级
				if (!ServiceDao.dao.upSupplyerInfoVersion(supplyerId))
					return false;
			}
		}
		// 多个权益信息rightList
		// 权益名称_需要金额_每卡每天上限_每卡每天每店上限_规则1_卡类型id=次数_卡类型id=次数,权益名称_需要金额_每卡每天上限_每卡每天每店上限_规则1_规则2_卡类型id=次数_卡类型id=次数
		String rightList = ct.getPara("rightList");
		String[] rightArr = rightList.split(",");
		if (rightArr != null) {
			for (String right : rightArr) {
				String[] rightAttrArr = right.split("_");
				Record ri = new Record();
				ri.set("ServiceID", serviceId);
				ri.set("Display", rightAttrArr[0]);
				ri.set("NeedMoney", Float.valueOf(rightAttrArr[1]) * 100);
				ri.set("DailyNumTop", Integer.valueOf(rightAttrArr[2]));
				ri.set("MonthNumTop", Integer.valueOf(rightAttrArr[3]));
				ri.set("YearNumTop", Integer.valueOf(rightAttrArr[4]));
				ri.set("DailyNumPerStore", Integer.valueOf(rightAttrArr[5]));
				ri.set("SelfRule1", Integer.valueOf(rightAttrArr[6]));
				ri.set("valid", VerifyStatus.valid.code);
				// 重复权益
				Map<String, Object> cv1 = new HashMap<String, Object>();
				cv1.put("Display", ri.get("Display"));
				cv1.put("ServiceID", ri.get("ServiceID"));
				if (JfinalUtil.findRecord("rightinfo", cv1) != null) {
					AddEntityException ae = new AddEntityException();
					ae.getMap().put("errorInfo", "添加的权益名称信息存在重复");
					throw ae;
				}
				if (!ServiceDao.dao.addRightInfo(ri))
					return false;
				Integer RightID = JfinalUtil.findRecord("rightinfo", cv1).getInt("RightID");
				// 添加权益规则
				for (int i = 7; i < rightAttrArr.length; i++) {
					String[] rightRule = rightAttrArr[i].split("=");
					Integer rank = Integer.valueOf(rightRule[0]);
					Integer count = Integer.valueOf(rightRule[1]);
					// 重复权益规则
					Map<String, Object> cv2 = new HashMap<String, Object>();
					cv2.put("RightID", RightID);
					cv2.put("Rank", rank);
					if (JfinalUtil.findRecord("rightcount", cv2) != null) {
						AddEntityException ae = new AddEntityException();
						ae.getMap().put("errorInfo", "添加的权益规则信息存在重复");
						throw ae;
					}
					if (!ServiceDao.dao.addRightRule(RightID, rank, count, VerifyStatus.noAuditRightRule.code))
						return false;
				}
			}
		}
		return true;
	}

	// TODO 如果该权益已经使用了，不能更改（主要是cardright里同步问题）
	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();
		// 服务基本信息
		rd.set("serviceId", ct.getParaToInt("serviceId"));
		rd.set("typeId", ct.getParaToInt("typeId"));
		rd.set("serviceName", ct.getPara("serviceName"));
		rd.set("startDate", DateUtil.formatDateTime(ct.getPara("startDate")));
		rd.set("endDate", DateUtil.formatDateTime(ct.getPara("endDate")));
		rd.set("serviceProvision", ct.getPara("serviceProvision"));
		// 重复服务
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("serviceName", rd.get("serviceName"));
		Record service = JfinalUtil.findRecord("service", cv);
		if (service != null && !service.getInt("serviceId").equals(rd.getInt("serviceId"))) {
			EditEntityException ae = new EditEntityException();
			ae.getMap().put("errorInfo", "修改的服务信息名称存在重复");
			throw ae;
		}
		if (!ServiceDao.dao.edit(rd))
			return false;
		Integer serviceId = rd.getInt("serviceId");
		// 多个商户关联 supplyerIds 1,3,54,5
		String supplyerIds = ct.getPara("supplyerIds");
		String[] supplyerIdArr = supplyerIds.split(",");
		if (supplyerIdArr != null) {
			// 之前关系改为失效
			ServiceDao.dao.editServiceSupplyerInfoInvalid(serviceId);
			// 添加或修改新的关系
			for (String supplyerId : supplyerIdArr) {
				if (!ServiceDao.dao.addOrEditServiceSupplyerInfo(serviceId, Integer.valueOf(supplyerId)))
					return false;
				// 商户版本升级
				if (!ServiceDao.dao.upSupplyerInfoVersion(supplyerId))
					return false;
			}
		}
		// 多个权益信息rightList 权益id_权益名称_需要金额_每卡每天上限_每卡每天每店上限_规则1_卡类型id-次数_卡类型id-次数
		String rightList = ct.getPara("rightList");
		String[] rightArr = rightList.split(",");
		if (rightArr != null) {
			List<Integer> needrights = new ArrayList<Integer>();
			/**
			 * 添加或修改权益
			 */
			for (String right : rightArr) {
				String[] rightAttrArr = right.split("_");
				Record ri = new Record();
				ri.set("ServiceID", serviceId);
				// 修改时
				String rightId = rightAttrArr[0];
				if (StringUtils.isNotBlank(rightId)) {
					needrights.add(Integer.parseInt(rightId));
					ri.set("RightID", Integer.valueOf(rightId));
				}
				ri.set("Display", rightAttrArr[1]);
				ri.set("NeedMoney", Float.valueOf(rightAttrArr[2]) * 100);
				ri.set("DailyNumTop", Integer.valueOf(rightAttrArr[3]));
				ri.set("MonthNumTop", Integer.valueOf(rightAttrArr[4]));
				ri.set("YearNumTop", Integer.valueOf(rightAttrArr[5]));
				ri.set("DailyNumPerStore", Integer.valueOf(rightAttrArr[6]));
				ri.set("SelfRule1", Integer.valueOf(rightAttrArr[7]));
				// 修改或添加时出现重复权益
				Map<String, Object> cv1 = new HashMap<String, Object>();
				cv1.put("Display", ri.get("Display"));
				cv1.put("ServiceID", ri.get("ServiceID"));
				Record rightinfo = JfinalUtil.findRecord("rightinfo", cv1);
				if (rightinfo != null) {
					EditEntityException ae = new EditEntityException();
					if (StringUtils.isBlank(rightId)) {
						ae.getMap().put("errorInfo", "添加的权益信息名称存在重复");
						throw ae;
					}
					if (StringUtils.isNotBlank(rightId)
							&& !rightinfo.getInt("RightID").equals(Integer.valueOf(rightId))) {
						ae.getMap().put("errorInfo", "修改的权益信息名称存在重复");
						throw ae;
					}
				}
				// 权益主键
				Integer RightID = null;
				// 修改权益时
				if (StringUtils.isNotBlank(rightId)) {
					if (!ServiceDao.dao.editRightInfo(ri))
						return false;
					RightID = Integer.valueOf(rightId);
					List<Integer> needRankList = new ArrayList<Integer>();
					// 修改或添加权益规则
					for (int i = 8; i < rightAttrArr.length; i++) {
						String[] rightRule = rightAttrArr[i].split("=");
						Integer rank = Integer.valueOf(rightRule[0]);
						Integer count = Integer.valueOf(rightRule[1]);
						if (!ServiceDao.dao.addOrEditRightRule(RightID, rank, count))
							return false;
						needRankList.add(rank);
					}
					// 不需要的删除-检查状态
					ServiceDao.dao.deleteRightRuleCheckVID(needRankList, RightID);

				} else {
					// 添加权益时
					ri.set("valid", VerifyStatus.valid.code);
					if (!ServiceDao.dao.addRightInfo(ri))
						return false;
					RightID = JfinalUtil.findRecord("rightinfo", cv1).getInt("RightID");
					needrights.add(RightID);
					// 添加权益规则
					for (int i = 8; i < rightAttrArr.length; i++) {
						String[] rightRule = rightAttrArr[i].split("=");
						Integer rank = Integer.valueOf(rightRule[0]);
						Integer count = Integer.valueOf(rightRule[1]);
						// 重复权益规则
						Map<String, Object> cv2 = new HashMap<String, Object>();
						cv2.put("RightID", RightID);
						cv2.put("Rank", rank);
						if (JfinalUtil.findRecord("rightcount", cv2) != null) {
							EditEntityException ae = new EditEntityException();
							ae.getMap().put("errorInfo", "添加的权益规则信息存在重复");
							throw ae;
						}
						if (!ServiceDao.dao.addRightRule(RightID, rank, count, VerifyStatus.noAuditRightRule.code))
							return false;
					}
				}

			}
			/**
			 * 检测是否有通过审核、已分配、停用、权益规则
			 */
			List<Integer> vidList = new ArrayList<Integer>();
			vidList.add(VerifyStatus.auditPassRightRule.code);
			vidList.add(VerifyStatus.assignedRightRule.code);
			vidList.add(VerifyStatus.stopUsingRightRule.code);
			List<Record> needRightRules = ServiceDao.dao.findRightRulelistForRightIdsAndVIds(needrights, serviceId,
					vidList);
			if (needRightRules.size() != 0) {
				StringBuilder errorInfo = new StringBuilder("删除服务出错!该服务有权益的规则在使用中或处理中:");
				for (Record nrd : needRightRules) {
					errorInfo.append("权益名称:" + nrd.getStr("Display") + " 卡类别:" + nrd.getStr("Description") + " 状态"
							+ nrd.getStr("VDescription") + "");
				}
				throw new EditEntityException(errorInfo.toString());
			}
			/**
			 * 修改不需要的权益的失效状态
			 */
			ServiceDao.dao.editRightInfoAndRightRuleInvalid(needrights, serviceId);
		}
		return true;
	}

	@Override
	public boolean delete(Object id, Controller ct) throws Exception {
		// 服务失效
		Integer serviceId = Integer.parseInt(((String) id));
		/**
		 * 检测是否有通过审核、已分配、停用、权益规则
		 */
		List<Integer> vidList = new ArrayList<Integer>();
		vidList.add(VerifyStatus.auditPassRightRule.code);
		vidList.add(VerifyStatus.assignedRightRule.code);
		vidList.add(VerifyStatus.stopUsingRightRule.code);
		List<Record> needRightRules = ServiceDao.dao.findRightRulelistForServiceIdAndVIds(serviceId, vidList);
		if (needRightRules.size() != 0) {
			StringBuilder errorInfo = new StringBuilder("删除服务出错!该服务有权益的规则在使用中或处理中:");
			for (Record rd : needRightRules) {
				errorInfo.append("权益名称:" + rd.getStr("rightName") + " 卡类别:" + rd.getStr("cardType") + " 状态"
						+ rd.getStr("VDescription") + "");
			}
			throw new DeleteEntityException(errorInfo.toString());
		}
		if (!ServiceDao.dao.delete(serviceId))
			return false;
		// 服务商家关系失效
		ServiceDao.dao.editServiceSupplyerInfoInvalid(serviceId);
		// 权益失效及其权益规则失效
		List<Integer> needrights = new ArrayList<Integer>();
		ServiceDao.dao.editRightInfoAndRightRuleInvalid(needrights, serviceId);
		return true;
	}

	/**
	 * 查询服务信息
	 */
	public void find(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));

		rd.set("serviceName", URLUtil.decodeUTF8(ct.getPara("serviceName")));
		rd.set("typeId", ct.getParaToInt("typeId"));
		Page<Record> page = ServiceDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询权益数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	/**
	 * 查询指定服务id的所有商户
	 */
	public void findSupplyerIdByServiceId(Controller ct) {
		Integer serviceId = ct.getParaToInt("serviceId");
		List<Record> list = ServiceDao.dao.findSupplyerIdByServiceId(serviceId);
		JfinalUtil.convertNullToNullString(list);
		ResultVo rv = new ResultVo(true, "查询指定服务商户数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * 查询指定服务id的所有权益及权益规则
	 */
	public void findRightAndRightRuleByServiceId(Controller ct) {
		Integer serviceId = ct.getParaToInt("serviceId");
		List<Record> list = ServiceDao.dao.findRightAndRightRuleByServiceId(serviceId);
		JfinalUtil.convertNullToNullString(list);
		this.editRightRuleStruct(list);
		ResultVo rv = new ResultVo(true, "查询指定服务权益及权益规则数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * 调整权益规则数据结构
	 * 
	 * @param list
	 */
	private void editRightRuleStruct(List<Record> list) {
		for (Record right : list) {
			String rightRules = right.get("rightRules");
			List<Map<String, Object>> rrList = new ArrayList<Map<String, Object>>();
			String[] rightRulesArr = rightRules.split(",");
			for (String rightRule : rightRulesArr) {
				String[] ruleArr = rightRule.split("_");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("typeName", ruleArr[0]);
				map.put("typeId", ruleArr[1]);
				map.put("count", ruleArr[2]);
				map.put("state", ruleArr[3]);
				rrList.add(map);
			}
			right.set("rightRules", rrList);
		}
	}

	/**
	 * 查询所有商户
	 * 
	 * @param ct
	 */
	public void findSupplyerIdName(Controller ct) {
		List<Record> list = ServiceDao.dao.findSupplyerIdName();
		ResultVo rv = new ResultVo(true, "查询商户数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * 查询所有服务类别
	 * 
	 * @param ct
	 */
	public void findServiceTypeIdName(Controller ct) {
		List<Record> list = ServiceDao.dao.findServiceTypeIdName();
		ResultVo rv = new ResultVo(true, "查询服务类型数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * 查询所有卡类型
	 * 
	 * @param ct
	 */
	public void findCardTypeIdName(Controller ct) {
		List<Record> list = ServiceDao.dao.findCardTypeIdName();
		ResultVo rv = new ResultVo(true, "查询卡类型数据成功", list, list.size());
		ct.renderJson(rv);
	}

}

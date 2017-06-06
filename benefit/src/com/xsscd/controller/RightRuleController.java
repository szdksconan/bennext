package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.RightRuleService;

@ControllerBind(controllerKey="/back/rightRule")
public class RightRuleController extends Controller {
	public void add(){
		RightRuleService.service.addProxy(this);
	}
	public void edit(){
		RightRuleService.service.editProxy(this);
	}
	public void delete(){
		RightRuleService.service.deleteBatchProxy(this);
	}
//	public void audit(){
//		RightRuleService.service.auditBatchProxy(this);
//	}
	//查询权益规则关联列表
	public void findLink(){
		RightRuleService.service.findLink(this);
	}
	//查询权益规则分配列表
	public void findAllocation(){
		RightRuleService.service.findAllocation(this);
	}
	//分配权益规则
	public void allocate(){
		RightRuleService.service.operateBatchProxy(this, "分配权益规则", "allocate");
	}
	/**
	 * 通过审核的权益
	 */
	public void findAuditPassRightIdName(){
		RightRuleService.service.findAuditPassRightIdName(this);
	}
	/**
	 * 所有的银行
	 */
	public void findBankIdName(){
		RightRuleService.service.findBankIdName(this);
	}
	/**
	 * 某个银行的所有卡类型
	 */
	public void findCardTypeIdName(){
		RightRuleService.service.findCardTypeIdName(this);
	}
	/**
	 *  -------------------------------------新版本----------------------------------------------------
	 */
	//查询所有权益规则审核列表
	public void findAudit(){
		RightRuleService.service.find(this);
	}
	//批量审核
	public void audit(){
		RightRuleService.service.operateBatchProxy(this, "审核权益规则", "auditRightRule");
	}
}

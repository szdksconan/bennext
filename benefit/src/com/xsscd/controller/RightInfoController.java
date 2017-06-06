package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.RightInfoService;

@ControllerBind(controllerKey = "/back/rightInfo")
public class RightInfoController extends Controller {
	public void add() {
		RightInfoService.service.addProxy(this);
	}

	public void edit() {
		RightInfoService.service.editProxy(this);
	}

	public void delete() {
		RightInfoService.service.deleteBatchProxy(this);
	}

	public void audit() {
		RightInfoService.service.auditBatchProxy(this);
	}

	public void find() {
		RightInfoService.service.find(this);
	}

	// 查询所有供应方(商户)id name列表
	// 后期加入分页查询
	public void findSupplyerIdName() {
		RightInfoService.service.findSupplyerIdName(this);
	}

	// 查询所有权益类型id name 列表
	public void findRightTypeIdName() {
		RightInfoService.service.findRightTypeIdName(this);
	}

	// 查询所有数量种类id name 列表
	public void findCountTypeIdName() {
		RightInfoService.service.findCountTypeIdName(this);
	}
}

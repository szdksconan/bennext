package com.xsscd.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.SupplyerInfoService;

@ControllerBind(controllerKey = "/back/supplyerInfo")
public class SupplyerInfoController extends Controller {
	private List<String> delFileList;

	public List<String> getDelFileList() {
		if (this.delFileList == null) {
			this.delFileList = new ArrayList<String>();
		}
		return this.delFileList;
	}

	public void setDelFileList(List<String> delFileList) {
		this.delFileList = delFileList;
	}

	public void add() {
		SupplyerInfoService.service.addProxy(this);
	}

	public void edit() {
		SupplyerInfoService.service.editProxy(this);
	}

	public void delete() {
		SupplyerInfoService.service.deleteBatchProxy(this);
	}

	public void find() {
		SupplyerInfoService.service.find(this);
	}

	// 查询所有商户类型id name 列表
	public void findSupplyerTypeIdName() {
		SupplyerInfoService.service.findSupplyerTypeIdName(this);
	}
}

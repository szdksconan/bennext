package com.xsscd.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.PublishService;

@ControllerBind(controllerKey = "/back/publish")
public class PublishController extends Controller {
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
		PublishService.service.addProxy(this);
	}

	public void edit() {
		PublishService.service.editProxy(this);
	}

	public void delete() {
		PublishService.service.deleteBatchProxy(this);
	}

	public void find() {
		PublishService.service.find(this);
	}

	// 查询所有信息分类id name列表
	// 后期加入分页查询
	public void findPublishClassIdName() {
		PublishService.service.findPublishClassIdName(this);
	}

	/**
	 * 前台系统--------------------------------------------------------------------
	 */
	// 前台查询信息
	@ActionKey("/front/publish/findPublishFront_no")
	public void findPublishFront() {
		PublishService.service.findPublishFront(this);
	}

	// 信息详情
	@ActionKey("/front/publish/findPublishDetailFront_no")
	public void findPublishDetailFront() {
		PublishService.service.findPublishDetailFront(this);
	}

}

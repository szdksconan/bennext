package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.RoleService;

@ControllerBind(controllerKey="/sys/role")
public class RoleController extends Controller {
	//查询角色权限的状态进行分页
	public void findRolePermissionState(){
		RoleService.service.findRolePermissionState(this);
	}
	//角色的权限分配
	public void editRolePermissionState(){
		RoleService.service.operateBatchProxy(this, "角色的权限分配", "editRolePermissionState");
	}
}


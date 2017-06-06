package com.xsscd.service;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.RoleDao;
import com.xsscd.exception.OperateEntityException;
import com.xsscd.util.JfinalUtil;
import com.xsscd.vo.ResultVo;

public class RoleService extends BaseService{
	
	public static RoleService service=new RoleService();

	public void findRolePermissionState(Controller ct) {
		Record rd=new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		// TODO 这里4为角色的数量
		int roleCount=4;
		rd.set("limit", ct.getParaToInt("limit")*roleCount);
		Page<Record> page= RoleDao.dao.findRolePermissionState( rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv= new ResultVo(true, "查询角色权限状态列表数据成功", page.getList(), page.getTotalRow()/roleCount);
		ct.renderJson(rv);
	}
	//角色id和权限id
	//前台格式 pid_rid_y,pid_rid_n,pid_rid_y
	public void editRolePermissionState(String str)throws Exception{
		//pid_rid_y
		String[] prs=str.split("_");
		Integer pid=Integer.valueOf(prs[0]),rid=Integer.valueOf(prs[1]);
		String state=prs[2];
		boolean isSuccess = false;
		if("y".equals(state)){
			isSuccess= RoleDao.dao.addRolePermission(rid,pid);
		}else
		if("n".equals(state)){
			isSuccess= RoleDao.dao.deleteRolePermission(rid,pid);
		}
		if(!isSuccess){
			String rName=Db.findById("role", rid).getStr("RName");
			String pName=Db.findById("permission", pid).getStr("PName");
			OperateEntityException oe=new OperateEntityException();
			oe.getMap().put("errorInfo", "分配权限出错!权限名称:"+pName+",角色名称:"+rName);
			throw oe;
		}
	}
}

package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.entity.User;
import com.xsscd.service.PlatformManagerService;

@ControllerBind(controllerKey="/back/platformManager")
public class PlatformManagerController extends Controller {
	private final PlatformManagerService pms = new PlatformManagerService();
	/**
	 * 登录
	 * @param uName 用户名
	 * @param psw 密码
	 * @return result  (true: 成功  false: 失败)
	 */
	public void login(){
		String uName=getPara("uName");
		String psw=getPara("psw");
		renderJson("{\"result\":"+pms.login(uName,psw)+"}");
	}
	/**
	 * 注册
	 * @param user 用户的所有基本信息
	 * @return result  (true: 成功  false: 失败)
	 * @return info  (失败或者成功的说明信息)
	 */
	public void register(){
		User regUser = new User();
		regUser.setuName(getPara("uName"));
		regUser.setuPassword(getPara("psw"));
		regUser.setRealName(getPara("realName"));
		regUser.setPhone(getPara("phone"));
		regUser.setEmail(getPara("email"));
		regUser.setRid(getParaToInt("RID"));
		regUser.setVid(getParaToInt("VID"));
		String regInfo = pms.register(regUser);
		String[] rs = regInfo.split(",");
		renderJson("{\"result\":"+rs[0]+",\"info\":"+rs[1]+"}");
	}
	/**
	 * 验证用户名是否可用
	 * @param uName 用户名
	 * @return result  (true: 成功  false: 失败)
	 * @return info  (失败或者成功的说明信息)
	 */
	public void validateUserName(){
		String uName=getPara("uName");
		String regInfo = pms.validateUserName(uName);
		String[] rs = regInfo.split(",");
		renderJson("{\"result\":"+rs[0]+",\"info\":"+rs[1]+"}");
	}
	public void getRoleList(){//
//		int rid = getParaToInt("RID");
		
	}
	public void getUserByCondition(){
		
	}
	public void updateUser(){
		
	}
	public void retriveCodeByEmail(){
		
	}
	public void retriveCodeByPhone(){
		
	}
	public void setNewPsw(){
		
	}
	public void deleteUser(){
		
	}
	
	
}

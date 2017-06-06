package com.xsscd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.service.UserService;
import com.xsscd.vo.ResultVo;

/**
 * 系统登录人员管理-包括持卡人和后台管理者
 * 
 * @author zengcy
 * 
 */
@ControllerBind(controllerKey = "/back/user")
public class UserController extends Controller {
	/**
	 * 权限相关
	 */
	// 获取当前用户所有的菜单
	@ActionKey("/back/user/findMenus_ro")
	public void findMenus() {
		UserService.service.findMenus(this);
	}

	// 获取指定菜单名称下的权限
	@ActionKey("/back/user/findPermissionsForMenuName_ro")
	public void findPermissionsForMenuName() {
		UserService.service.findPermissionsForMenuName(this);
	}

	/**
	 * ---------------------------------后台用户------------------------------------------------------------
	 */
	/**
	 * 后台修改登录用户信息
	 */
	public void editLoginUserInfo() {
		UserService.service.operateProxy(this, "后台修改登录用户信息", "backEditLoginUserInfo");
	}

	// 查询用户信息_ro表示不需权限检查-也不需分配权限
	@ActionKey("/back/user/findUserInfo_ro")
	public void findUserInfo() {
		Record bu = (Record) this.getSessionAttr("backUser");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UName", bu.get("UName"));
		map.put("RealName", bu.get("RealName"));
		map.put("Phone", bu.get("Phone"));
		map.put("Email", bu.get("Email"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
		ResultVo rv = new ResultVo(true, "查询持卡人基本信息数据成功", list, 1);
		this.renderJson(rv);
	}

	public void addBackUser() {
		UserService.service.addProxy(this);
	}

	public void editBackUser() {
		UserService.service.editProxy(this);
	}

	public void deleteBackUser() {
		UserService.service.deleteBatchProxy(this);
	}

	public void auditBackUser() {
		UserService.service.auditBatchProxy(this);
	}

	public void findBackUser() {
		UserService.service.findBackUser(this);
	}

	// 查询后台用户的角色列表
	public void findBackUserRoleIdName() {
		UserService.service.findBackUserRoleIdName(this);
	}

	// 查询后台用户的状态列表
	public void findBackUserVerifyStatusIdName() {
		UserService.service.findBackUserVerifyStatusIdName(this);
	}

	// 后台用户登录
	@ActionKey("/back/user/loginBackUser_no")
	public void loginBackUser() {
		UserService.service.loginBackUser(this);
	}

	// 修改当前用户密码-通过原密码
	public void editPwdForOldPwd() throws Exception {
		UserService.service.operateProxy(this, "通过原密码修改密码", "editPwdForOldPwd");
	}

	// 发送修改密码验证码
	@ActionKey("/back/user/sendVerifyCode_no")
	public void sendVerifyCode() {
		UserService.service.operateProxy(this, "发送修改密码验证码", "sendVerifyCode");
	}

	// 修改已忘记的密码（已经忘记密码）-通过邮箱或手机号验证
	@ActionKey("/back/user/editPwdForVerifyCode_no")
	public void editPwdForVerifyCode() {
		UserService.service.operateProxy(this, "通过邮箱或手机号修改密码", "editPwdForVerifyCode");
	}

	// 验证用户名是否存在
	@ActionKey("/back/user/validateUName_no")
	public void validateUName() {
		UserService.service.validateUName(this);
	}

	// 验证手机号是否存在
	@ActionKey("/back/user/validatePhone_no")
	public void validatePhone() {
		UserService.service.validatePhone(this);
	}

	// 验证邮箱是否存在
	@ActionKey("/back/user/validateEmail_no")
	public void validateEmail() {
		UserService.service.validateEmail(this);
	}

	// 查询 发卡银行和 卡名称
	public void findCardType() {
		UserService.service.findBankAndCardName(this);
	}

	// 注销
	public void loginOut() {
		UserService.service.loginOut(this);
	}

	/**
	 * ---------------------------------持卡人管理------------------------------------------------------------
	 */
	public void addCardUser() {
		UserService.service.operateProxy(this, "添加持卡人", "addCardUser");
	}

	public void editCardUser() {
		UserService.service.operateProxy(this, "修改持卡人", "editCardUser");
	}

	public void deleteCardUser() {
		UserService.service.operateBatchProxy(this, "删除持卡人", "deleteCardUser");
	}

	public void findCardUser() {
		UserService.service.findCardUser(this);
	}

	// 绑卡
	// public void backBindCard(){
	// UserService.service.operateProxy(this, "后台绑卡", "backBindCard");
	// }
	// 查询所有持卡人所有卡权益信息
	public void findCardRight() {
		UserService.service.findCardRight(this);
	}

	/**
	 * 后台查询服务支持的商家列表
	 */
	public void findServiceSupplyerList() {
		UserService.service.findServiceSupplyerList(this);
	}

	/**
	 * 前台系统------------------------------------------------------------------------------------------------------
	 */
	// 修改当前用户密码-通过原密码
	@ActionKey("/front/user/editPwdForOldPwd")
	public void frontEditPwdForOldPwd() throws Exception {
		UserService.service.operateProxy(this, "通过原密码修改密码", "editPwdForOldPwd");
	}

	// 发送修改密码验证码
	@ActionKey("/front/user/sendVerifyCode_no")
	public void frontSendVerifyCode() {
		UserService.service.operateProxy(this, "发送修改密码验证码", "sendVerifyCode");
	}

	// 修改已忘记的密码（已经忘记密码）-通过邮箱或手机号验证
	@ActionKey("/front/user/editPwdForVerifyCode_no")
	public void frontEditPwdForVerifyCode() {
		UserService.service.operateProxy(this, "通过邮箱或手机号修改密码", "editPwdForVerifyCode");
	}

	// 验证用户名是否存在
	@ActionKey("/front/user/validateUName_no")
	public void frontValidateUName() {
		UserService.service.validateUName(this);
	}

	// 验证手机号是否存在
	@ActionKey("/front/user/validatePhone_no")
	public void frontValidatePhone() {
		UserService.service.validatePhone(this);
	}

	// 验证邮箱是否存在
	@ActionKey("/front/user/validateEmail_no")
	public void frontfrontalidateEmail() {
		UserService.service.validateEmail(this);
	}

	// 查询 发卡银行和 卡名称
	@ActionKey("/front/user/findCardType")
	public void frontFindCardType() {
		UserService.service.findBankAndCardName(this);
	}

	// 注销
	@ActionKey("/front/user/loginOut")
	public void frontLoginOut() {
		UserService.service.loginOut(this);
	}

	// 持卡人登录
	@ActionKey("/front/user/loginCardUser_no")
	public void loginCardUser() {
		UserService.service.loginCardUser(this);
	}

	// 持卡人注册
	@ActionKey("/front/user/registerCardUser_no")
	public void registerCardUser() {
		UserService.service.operateProxy(this, "注册持卡人", "registerCardUser");
	}

	// 查询用户基本信息-包括卡列表
	@ActionKey("/front/user/findCardUserDetail")
	public void findCardUserDetail() {
		UserService.service.findCardUserDetail(this);
	}

	// 登录的持卡人绑卡
	@ActionKey("/front/user/frontBindCard")
	public void frontBindCard() {
		UserService.service.operateProxy(this, "登录的持卡人绑卡", "frontBindCard");
	}

	@ActionKey("/front/user/frontBindCardCheckVcode")
	public void frontBindCardCheckVcode() {
		UserService.service.operateProxy(this, "登录的持卡人绑卡验证码检查", "frontBindCardCheckVcode");
	}

	// 登录的持卡人解绑卡片
	@ActionKey("/front/user/frontUnBindCard")
	public void frontUnBindCard() {
		UserService.service.operateProxy(this, "登录的持卡人解绑卡片", "frontUnBindCard");
	}

	// 查询登录的持卡人所有卡权益信息列表
	@ActionKey("/front/user/findCardUserCardRight")
	public void findCardUserCardRight() {
		UserService.service.findCardUserCardRight(this);
	}

	// 查询登录的持卡人 某个权益详情及所有卡次数
	@ActionKey("/front/user/findCardUserRightDetail")
	public void findCardUserRightDetail() {
		UserService.service.findCardUserRightDetail(this);
	}

	/**
	 * 前台修改登录用户信息
	 */
	@ActionKey("/front/user/editLoginUserInfo")
	public void frontEditLoginUserInfo() {
		UserService.service.operateProxy(this, "前台修改登录用户信息", "frontEditLoginUserInfo");
	}
}

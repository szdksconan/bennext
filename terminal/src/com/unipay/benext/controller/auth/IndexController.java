package com.unipay.benext.controller.auth;


import com.unipay.benext.framework.constant.GlobalConstant;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.framework.tool.SessionInfo;
import com.unipay.benext.framework.web.BaseController;
import com.unipay.benext.pageModel.auth.User;
import com.unipay.benext.service.auth.UserServiceI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {

	@Autowired
	private UserServiceI userService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		if ((sessionInfo != null) && (sessionInfo.getId() != null)) {
			return "/index";
		}
		return "/login";
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public Json login(User user,HttpServletRequest request) {
		Json j = new Json();

		if (user.getVerify() != null && !user.getVerify().equals("")) {
			if (!user.getVerify().equalsIgnoreCase(request.getSession().getAttribute("validateCode").toString())) {
				j.setMsg("验证码错误!");
				return j;
			}
		}
		User sysuser = userService.login(user);
		if (sysuser != null) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");

			SessionInfo sessionInfo = new SessionInfo();
			sessionInfo.setId(sysuser.getId());
			sessionInfo.setLoginname(sysuser.getLoginName());
			sessionInfo.setName(sysuser.getName());
			if(sysuser.getRoleName().equals("超级管理员")){
				sessionInfo.setSuperAadmin(true);
			}
			request.getSession().setAttribute(GlobalConstant.SESSION_INFO, sessionInfo);
		}else{
			if(StringUtils.isBlank(user.getLoginName())){
				j.setMsg("账号不能为空!");
			}
			else if(StringUtils.isBlank(user.getLoginPwd())){
				j.setMsg("密码不能为空!");
			}
			else
				j.setMsg("账号或密码错误!");
		}
		return j;
	}

	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}
}

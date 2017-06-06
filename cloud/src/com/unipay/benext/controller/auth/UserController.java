package com.unipay.benext.controller.auth;

import com.unipay.benext.framework.constant.GlobalConstant;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.framework.tool.SessionInfo;
import com.unipay.benext.service.auth.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceI userService;
	
	
	@RequestMapping("/editPwdPage")
	public String editPwdPage(HttpServletRequest request) {
		return "/admin/userEditPwd";
	}
	
	@RequestMapping("/editUserPwd")
	@ResponseBody
	public Json editUserPwd(HttpServletRequest request, String oldPwd, String pwd) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(GlobalConstant.SESSION_INFO);
		Json j = new Json();
		try {
			boolean b =  userService.editUserPwd(sessionInfo, oldPwd, pwd);
			if(b){
				j.setSuccess(true);
				j.setMsg("密码修改成功！");
			}else{
				j.setMsg("密码修改失败，原密码输入错误");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}

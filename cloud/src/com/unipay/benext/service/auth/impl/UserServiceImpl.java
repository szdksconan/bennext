package com.unipay.benext.service.auth.impl;

import com.unipay.benext.dao.BaseDaoI;
import com.unipay.benext.framework.tool.SessionInfo;
import com.unipay.benext.model.basic.Tuser;
import com.unipay.benext.pageModel.auth.User;
import com.unipay.benext.service.auth.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserServiceI {

	@Autowired
	private BaseDaoI<Tuser> userDao;

	@Override
	public User login(User user) {
//		if ("admin".equals(user.getLoginName())&&"admin".equals(user.getLoginPwd())){
//			user.setId(1L);
//			user.setName("超级管理员");
//			user.setRoleName("超级管理员");
//			return user;
//		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", user.getLoginName());
		params.put("loginPwd", user.getLoginPwd());
		Tuser t = userDao.get("from Tuser t where t.status = 0 and t.isEnabled = 0 and binary(t.loginName) = :loginName and binary(t.loginPwd) = :loginPwd", params);
		if (t != null) {
			user.setId(1L);
			user.setName("超级管理员");
			user.setRoleName("超级管理员");
			return user;
		}
		return null;
	}

	@Override
	public boolean editUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd) {
		Tuser u = userDao.get(Tuser.class, sessionInfo.getId());
		if (u.getLoginPwd().equalsIgnoreCase(oldPwd)) {// 说明原密码输入正确
			u.setLoginPwd(pwd);
			return true;
		}
		return false;
	}
}

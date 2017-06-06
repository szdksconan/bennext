package com.unipay.benext.service.auth;

import com.unipay.benext.framework.tool.SessionInfo;
import com.unipay.benext.pageModel.auth.User;

public interface UserServiceI {
	public User login(User user);

	public boolean editUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd);
}

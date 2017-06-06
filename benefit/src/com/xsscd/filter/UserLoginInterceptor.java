package com.xsscd.filter;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.vo.ResultVo;

public class UserLoginInterceptor implements Interceptor {
	@Override
	public void intercept(ActionInvocation ai) {
		Controller ct = ai.getController();
		Record backUser = ct.getSessionAttr("backUser");
		Record cardUser = ct.getSessionAttr("cardUser");
		String ak = ai.getActionKey();
		if (backUser == null && cardUser == null && !ak.contains("_no") && !ak.contains("POSManager") && !ak.contains("PrServiceManager") && StringUtils.isBlank(ai.getController().getPara("macValue"))) {
			ResultVo rv = new ResultVo(false, "没有登录");
			ct.renderJson(rv);
		} else {
			ai.invoke();
		}
	}

}

package com.xsscd.filter;

import java.util.List;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.dao.RoleDao;
import com.xsscd.vo.ResultVo;

public class PermissionInterceptor implements Interceptor {
	@Override
	public void intercept(ActionInvocation ai) {
		Controller ct = ai.getController();
		List<String> permissionUris = ct.getSessionAttr("permissionUris");
		String uri = ai.getActionKey();
		boolean havePermission = false;
		// 不需检查权限-无需登录请求、或pos请求或前台请求、后台无需权限控制_ro
		if (uri.contains("_no") || uri.contains("_ro") || uri.contains("POSManager")|| uri.contains("PrServiceManager") || uri.contains("/front")) {
			havePermission = true;
		} else {
			// 需要检测权限
			for (String URI : permissionUris) {
				if (uri.contains(URI)) {
					havePermission = true;
					break;
				}
			}
		}
		if (havePermission) {
			ai.invoke();
		} else {
			// ---/back/service/findCardTypeIdName
			Record p = RoleDao.dao.findPermisionByUri(uri);
			String pName = p == null ? "" : p.getStr("PName") + "->";
			ResultVo rv = new ResultVo(false, pName + "没有权限 ");
			ct.renderJson(rv);
		}
	}
}

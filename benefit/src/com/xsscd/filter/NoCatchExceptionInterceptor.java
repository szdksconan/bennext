package com.xsscd.filter;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.xsscd.base.BaseService;
import com.xsscd.vo.ResultVo;

public class NoCatchExceptionInterceptor implements Interceptor {
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller=ai.getController();
		try {
			ai.invoke();			
		}
		catch (Exception e) {
			e.printStackTrace();
			BaseService.log.error("未处理的异常日志——Interceptor中捕捉到异常", e);
			ResultVo rv=new ResultVo(false, "系统繁忙,请稍候尝试");
			controller.renderJson(rv);
		}
	}

}

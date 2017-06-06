package com.xsscd.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Record;

public class UserLoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Record backUser = (Record) req.getSession().getAttribute("backUser");
		Record cardUser = (Record) req.getSession().getAttribute("cardUser");
		// 只处理后台.html请求是否登录
		String url = req.getRequestURL().toString();
		if (backUser == null && StringUtils.contains(url, "/back") && StringUtils.contains(url, ".html")
				&& !StringUtils.contains(url, "login.html") && !StringUtils.contains(url, "findpasswd.html")) {
			resp.sendRedirect(req.getContextPath() + "/back/login.html");
		} else if (cardUser == null
				&& (StringUtils.contains(url, "huiyuan.html") || StringUtils.contains(url, "quanyi.html"))) {
			resp.sendRedirect(req.getContextPath() + "/front/login.html");
		} else {
			fc.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

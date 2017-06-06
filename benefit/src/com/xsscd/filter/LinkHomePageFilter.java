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

public class LinkHomePageFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
			ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 只处理后台.html请求是否登录
		String url = req.getRequestURL().toString();
		// BaseService.log.info(url + req + req.getParameter("menuName"));
		String[] urlArr = url.split(req.getContextPath());
		// 跳入主页
		if (urlArr[1].equals("/back") || urlArr[1].equals("/back/")) {
			resp.sendRedirect(req.getContextPath() + "/back/3110.html");
		} else if (urlArr[1].equals("/front") || urlArr[1].equals("/front/")) {
			resp.sendRedirect(req.getContextPath() + "/front/index.html");
		} else {
			fc.doFilter(req, resp);
		}

	}

	// public static void main(String[] args) {
	// // %E5%B9%B3%E5%8F%B0%E7%AE%A1%E7%90%86%E5%91%98%E6%B3%A8%E5%86%8C%E5%AE%A1%E6%A0%B8
	// String s = ("%E5%B9%B3%E5%8F%B0%E7%AE%A1%E7%90%86%E5%91%98%E6%B3%A8%E5%86%8C%E5%AE%A1%E6%A0%B8");
	// System.out.println("LinkHomePageFilter.main()" + s);
	// }

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

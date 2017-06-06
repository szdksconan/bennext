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

import com.xsscd.util.URLUtil;

public class DownFileFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String[] uriArr = uri.split("files");
		String realUri = URLUtil.decodeUTF8(uriArr[1].substring(1));
		// 检查服务器tomcat编码
		resp.setHeader("Content-Type", "application/octet-stream");
		// fc.doFilter(req, resp);
		// include这里共享response不能使用forward
		req.getRequestDispatcher(realUri).include(request, response);
		// resp.sendRedirect(URLUtil.decodeUTF8(uri));

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

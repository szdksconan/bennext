package com.xsscd.filter;

import javax.servlet.*;
import java.io.IOException;

public class InterfaceFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
			ServletException {
		fc.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

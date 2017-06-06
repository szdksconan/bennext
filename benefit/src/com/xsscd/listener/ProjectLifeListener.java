package com.xsscd.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ProjectLifeListener  implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//设置日志路径
		String path= sce.getServletContext().getRealPath("logs");
		System.setProperty("gzLogPath",path );
	}
	
}

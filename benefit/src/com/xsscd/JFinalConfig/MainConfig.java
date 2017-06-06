package com.xsscd.JFinalConfig;

import java.io.File;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.xsscd.autoTask.TaskManage;
import com.xsscd.base.BaseService;
import com.xsscd.filter.NoCatchExceptionInterceptor;
import com.xsscd.filter.PermissionInterceptor;
import com.xsscd.filter.UserLoginInterceptor;

/**
 * 项目主要信息配置类
 * 
 * @author zengcy
 * 
 */

public class MainConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
	}

	@Override
	public void afterJFinalStart() {
		// 配置
		// 附件文件夹
		String publishFiles = JFinal.me().getServletContext().getRealPath(File.separator + "files");
		File f = new File(publishFiles);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		BaseService.log.info("任务开启前" + Thread.activeCount());
		// 开启所有任务
		TaskManage.tm.startAllTask();
		BaseService.log.info("任务开启后" + Thread.activeCount());

	}

	@Override
	public void beforeJFinalStop() {
		// 停止所有任务
		TaskManage.tm.stopAllTask();
	}

	@Override
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
		// me.add("/rightData", RightDataController.class);
		// me.add("/platformManager", PlatformManagerController.class);
		// me.add("/rightManager", RightManagerController.class);
	}

	@Override
	public void configHandler(Handlers me) {
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new NoCatchExceptionInterceptor());
		me.add(new UserLoginInterceptor());
		me.add(new PermissionInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {
		loadPropertyFile("mysql.properties");
		DruidPlugin c3p0Plugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"),
				getProperty("jdbcDriver"));
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(true);

		SqlInXmlPlugin si = new SqlInXmlPlugin();
		me.add(si);
	}
}

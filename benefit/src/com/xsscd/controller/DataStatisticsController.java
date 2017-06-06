package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.DataStatisticsService;

/**
 * 数据统计
 * 
 * @author zengcy
 * 
 */
@ControllerBind(controllerKey = "/back/dataStatistics")
public class DataStatisticsController extends Controller {
	/**
	 * 查询POS权益消费记录
	 */
	public void findPOSRightRecord() {
		DataStatisticsService.service.findPOSRightRecord(this);
	}

	/**
	 * 导出POS权益消费记录excel
	 */
	public void exportPOSRightRecordExcel() {
		DataStatisticsService.service.exportPOSRightRecordExcel(this);
	}

	/**
	 * 查询POS权益消费月统计
	 */
	public void findPOSRightMonthRecord() {
		DataStatisticsService.service.findPOSRightMonthRecord(this);
	}

	/**
	 * 导出POS权益消费月统计excel
	 */
	public void exportPOSRightMonthRecordExcel() {
		DataStatisticsService.service.exportPOSRightMonthRecordExcel(this);
	}
}

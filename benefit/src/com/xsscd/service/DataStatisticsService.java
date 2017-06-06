package com.xsscd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.DataStatisticsDao;
import com.xsscd.util.ExcelExportUtil;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;

public class DataStatisticsService extends BaseService {

	public static DataStatisticsService service = new DataStatisticsService();

	/**
	 * XXX 有时间封装-统一的参数record
	 * 
	 * @param ct
	 */
	public void findPOSRightRecord(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("BankName", URLUtil.decodeUTF8(ct.getPara("BankName")));
		rd.set("CardNum", URLUtil.decodeUTF8(ct.getPara("CardNum")));
		rd.set("RightName", URLUtil.decodeUTF8(ct.getPara("RightName")));
		rd.set("TermCode", URLUtil.decodeUTF8(ct.getPara("TermCode")));
		rd.set("SupplyerName", URLUtil.decodeUTF8(ct.getPara("SupplyerName")));
		rd.set("MerchantCode", URLUtil.decodeUTF8(ct.getPara("MerchantCode")));
		rd.set("startDate", ct.getPara("startDate"));
		rd.set("endDate", ct.getPara("endDate"));
		Page<Record> page = DataStatisticsDao.dao.findPOSRightRecord(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询POS权益消费记录", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void findPOSRightMonthRecord(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("BankName", URLUtil.decodeUTF8(ct.getPara("BankName")));
		rd.set("RightName", URLUtil.decodeUTF8(ct.getPara("RightName")));
		rd.set("TermCode", URLUtil.decodeUTF8(ct.getPara("TermCode")));
		rd.set("SupplyerName", URLUtil.decodeUTF8(ct.getPara("SupplyerName")));
		rd.set("MerchantCode", URLUtil.decodeUTF8(ct.getPara("MerchantCode")));
		rd.set("startMonth", ct.getPara("startMonth"));
		rd.set("endMonth", ct.getPara("endMonth"));
		Page<Record> page = DataStatisticsDao.dao.findPOSRightMonthRecord(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询POS权益消费月统计", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void exportPOSRightRecordExcel(Controller ct) {
		Record rd = new Record();
		rd.set("BankName", URLUtil.decodeUTF8(ct.getPara("BankName")));
		rd.set("CardNum", URLUtil.decodeUTF8(ct.getPara("CardNum")));
		rd.set("RightName", URLUtil.decodeUTF8(ct.getPara("RightName")));
		rd.set("TermCode", URLUtil.decodeUTF8(ct.getPara("TermCode")));
		rd.set("SupplyerName", URLUtil.decodeUTF8(ct.getPara("SupplyerName")));
		rd.set("MerchantCode", URLUtil.decodeUTF8(ct.getPara("MerchantCode")));
		rd.set("startDate", ct.getPara("startDate"));
		rd.set("endDate", ct.getPara("endDate"));
		List<Record> listRd = DataStatisticsDao.dao.findAllPOSRightRecord(rd);
		JfinalUtil.convertNullToNullString(listRd);
		/**
		 * 下载
		 */
		ExcelExportUtil e = new ExcelExportUtil();
		e.addTableHead("发卡银行", "银行卡号", "权益名称", "交易时间", "终端号", "商户号", "商户名称");
		String fileName = "POS权益消费记录.xls";
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (Record record : listRd) {
			record.set("01BankName", record.get("BankName"));
			record.set("02CardNum", record.get("CardNum"));
			record.set("03Display", record.get("Display"));
			record.set("04PayDate", record.get("PayDate"));
			record.set("05TermCode", record.get("TermCode"));
			record.set("06MerchantCode", record.get("MerchantCode"));
			record.set("07SupplyerName", record.get("SupplyerName"));
			listMap.add(record.getColumns());
		}
		ct.renderNull();
		super.exportExcel(true, fileName, e, listMap, 1, ct.getResponse());

	}

	public void exportPOSRightMonthRecordExcel(Controller ct) {
		Record rd = new Record();
		rd.set("BankName", URLUtil.decodeUTF8(ct.getPara("BankName")));
		rd.set("RightName", URLUtil.decodeUTF8(ct.getPara("RightName")));
		rd.set("TermCode", URLUtil.decodeUTF8(ct.getPara("TermCode")));
		rd.set("SupplyerName", URLUtil.decodeUTF8(ct.getPara("SupplyerName")));
		rd.set("MerchantCode", URLUtil.decodeUTF8(ct.getPara("MerchantCode")));
		rd.set("startMonth", ct.getPara("startMonth"));
		rd.set("endMonth", ct.getPara("endMonth"));
		List<Record> listRd = DataStatisticsDao.dao.findAllPOSRightMonthRecord(rd);
		JfinalUtil.convertNullToNullString(listRd);
		/**
		 * 下载
		 */
		ExcelExportUtil e = new ExcelExportUtil();
		e.addTableHead("发卡银行", "交易年月", "权益名称", "商户名称", "商户号", "终端号", "交易笔数");
		String fileName = "POS权益消费月统计.xls";
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (Record record : listRd) {
			record.set("01BankName", record.get("BankName"));
			record.set("02YearMonth", record.get("YearMonth"));
			record.set("03Display", record.get("Display"));
			record.set("04SupplyerName", record.get("SupplyerName"));
			record.set("05MerchantCode", record.get("MerchantCode"));
			record.set("06TermCode", record.get("TermCode"));
			record.set("07rdCount", record.get("rdCount") + "");
			listMap.add(record.getColumns());
		}
		ct.renderNull();
		super.exportExcel(true, fileName, e, listMap, 1, ct.getResponse());
	}
}

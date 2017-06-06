package com.xsscd.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.service.POSManagerService;

@ControllerBind(controllerKey = "/POSManager")
public class POSManagerController extends Controller {
	private static Logger log = LoggerFactory.getLogger(Class.class);
	private static POSManagerService pms = new POSManagerService();

	/**
	 * 缴费
	 */
	public void pay() {
		// 入参：终端号、商户号、银行卡号、选择的项目ID、缴费金额、系统跟踪号 、检索参考号 、受理机构代码 、发送机构代码 、清算日期 、本地交易日期 、本地交易时间 、手机号
		// |11111001|898520148140001|62293901000119887|11|7000|155327|141217155327|00167000|D0000170|1217|1217|155327|13984040106|
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			// String paras = getPara("paras");
			// String[] tmps = paras.trim().split("\\|");
			Record rs = new Record();
			if (tmps.length == 15) {
				rs.set("TermCode", tmps[1]);
				rs.set("MerchantCode", tmps[2]);
				rs.set("CardNum", tmps[3]);
				rs.set("RightID", tmps[4]);
				rs.set("Amount", tmps[5]);
				rs.set("SysTrackNum", tmps[6]);
				rs.set("RetRefNum", tmps[7]);// Retrieve reference number
				rs.set("AcceptCode", tmps[8]);// An accepting institution code
				rs.set("SenderCode", tmps[9]);
				rs.set("LiquiDate", tmps[10]);// The date of liquidation
				rs.set("LocalTransDate", tmps[11]);// The local transaction date
				rs.set("LocalTransTime", tmps[12]);
				rs.set("Phone", tmps[13]);
				renderText(pms.pay(rs));
			} else {
				renderText("|99|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			log.error("参数获取失败", e);
		}
	}

	/**
	 * 冲正
	 */
	public void remedy() {
		// 入参：终端号、商户号、银行卡号、选择的项目ID、缴费金额、系统跟踪号 、检索参考号 、受理机构代码 、发送机构代码 、清算日期 、本地交易日期 、本地交易时间
		// |11111001|898520148140001|62293901000119887|11|7000|155327|141217155327|00167000|D0000170|1217|1217|155327|
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			// String paras = getPara("paras");
			// String[] tmps = paras.trim().split("\\|");
			Record rs = new Record();
			if (tmps.length == 14) {
				rs.set("TermCode", tmps[1]);
				rs.set("MerchantCode", tmps[2]);
				rs.set("CardNum", tmps[3]);
				rs.set("RightID", tmps[4]);
				rs.set("Amount", tmps[5]);
				rs.set("SysTrackNum", tmps[6]);
				rs.set("RetRefNum", tmps[7]);// Retrieve reference number
				rs.set("AcceptCode", tmps[8]);// An accepting institution code
				rs.set("SenderCode", tmps[9]);
				rs.set("LiquiDate", tmps[10]);// The date of liquidation
				rs.set("LocalTransDate", tmps[11]);// The local transaction date
				rs.set("LocalTransTime", tmps[12]);
				renderText(pms.remedy(rs));
			} else {
				renderText("|99|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			log.error("参数获取失败", e);
		}
	}

	/**
	 * 刷卡
	 */
	public void consume() {
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			Record rs = new Record();
			if (tmps.length == 16) {
				rs.set("TermCode", tmps[1]);
				rs.set("MerchantCode", tmps[2]);
				rs.set("CardNum", tmps[3]);
				rs.set("RightID", tmps[4]);
				rs.set("Amount", tmps[5]);
				rs.set("SysTrackNum", tmps[6]);
				rs.set("RetRefNum", tmps[7]);// Retrieve reference number
				rs.set("AcceptCode", tmps[8]);// An accepting institution code
				rs.set("SenderCode", tmps[9]);
				rs.set("LiquiDate", tmps[10]);// The date of liquidation
				rs.set("LocalTransDate", tmps[11]);// The local transaction date
				rs.set("LocalTransTime", tmps[12]);
				rs.set("Phone", tmps[13]);
				rs.set("Version", tmps[14]);
				renderText(pms.consume(rs));
			} else {
				renderText("|99|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			log.error("参数获取失败", e);
		}
	}

	/**
	 * 参数下载
	 */
	public void download() {
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			Record rs = new Record();
			if (tmps.length == 4) {
				rs.set("TermCode", tmps[1]);
				rs.set("MerchantCode", tmps[2]);
				renderText(pms.download(rs));
			} else {
				renderText("|99|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			log.error("参数获取失败", e);
		}
	}
}

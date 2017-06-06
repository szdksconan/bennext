package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.service.RightManagerService;

@ControllerBind(controllerKey="/back/rightManager")
public class RightManagerController extends Controller {
	private final RightManagerService rms = new RightManagerService();
	/**
	 * 缴费（权益消费）
	 * @param 入参：终端号、商户号、银行卡号、选择的项目ID、缴费金额、系统跟踪号 、检索参考号 、受理机构代码 、发送机构代码 、清算日期 、本地交易日期 、本地交易时间 
	 * @return 出参：|返回码|返回码描述|备注信息|
	 */
	public void pay(){
		//入参：终端号、商户号、银行卡号、选择的项目ID、缴费金额、系统跟踪号 、检索参考号 、受理机构代码 、发送机构代码 、清算日期 、本地交易日期 、本地交易时间 
		//|11111001|898520148140001|62293901000119887|11|7000|155327|141217155327|00167000|D0000170|1217|1217|155327|
		try {
			String paras = getPara("paras");
			Record rs = new Record();
			String[] tmps = paras.trim().split("\\|");
			if(tmps.length == 13){
				rs.set("TerminalNum", tmps[1]);
				rs.set("SupplyerID", tmps[2]);
				rs.set("CardNum", tmps[3]);
				rs.set("RightID", tmps[4]);
				rs.set("Amount", tmps[5]);
				rs.set("SysTrackNum", tmps[6]);
				rs.set("RetRefNum", tmps[7]);//Retrieve reference number
				rs.set("AcceptCode", tmps[8]);//An accepting institution code
				rs.set("SenderCode", tmps[9]);
				rs.set("LiquiDate", tmps[10]);//The date of liquidation
				rs.set("LocalTransDate", tmps[11]);//The local transaction date
				rs.set("LocalTransTime", tmps[12]);
				renderText(rms.pay(rs));
			}else{
				renderText("|99|缴费失败|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|缴费失败|参数获取失败|");
		}
	}
	public void remedy(){
		//入参：终端号、商户号、银行卡号、选择的项目ID、缴费金额、系统跟踪号 、检索参考号 、受理机构代码 、发送机构代码 、清算日期 、本地交易日期 、本地交易时间 
				//|11111001|898520148140001|62293901000119887|11|7000|155327|141217155327|00167000|D0000170|1217|1217|155327|
				try {
					String paras = getPara("paras");
					Record rs = new Record();
					String[] tmps = paras.split("\\|");
					if(tmps.length == 13){
						rs.set("TerminalNum", tmps[1]);
						rs.set("SupplyerID", tmps[2]);
						rs.set("CardNum", tmps[3]);
						rs.set("RightID", tmps[4]);
						rs.set("Amount", tmps[5]);
						rs.set("SysTrackNum", tmps[6]);
						rs.set("RetRefNum", tmps[7]);//Retrieve reference number
						rs.set("AcceptCode", tmps[8]);//An accepting institution code
						rs.set("SenderCode", tmps[9]);
						rs.set("LiquiDate", tmps[10]);//The date of liquidation
						rs.set("LocalTransDate", tmps[11]);//The local transaction date
						rs.set("LocalTransTime", tmps[12]);
						renderText(rms.remedy(rs));
					}else{
						renderText("|99|缴费失败|非法参数|");
					}
				} catch (Exception e) {
					renderText("|99|冲正失败|非法参数|");
				}
	}
}

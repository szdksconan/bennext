package com.xsscd.test;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

public class TestQYPT {
	// 参数下载：POSManager/download
	/**
	 * |终端号|商户号|
	 */
	// 刷卡：POSManager/consume
	/**
	 * |终端号|商户号|银行卡号|选择的项目ID|缴费金额|系统跟踪号|检索参考号| 受理机构代码|发送机构代码|清算日期|本地交易日期|本地交易时间|手机号|版本号|
	 */
	// 缴费：POSManager/pay
	/**
	 * |终端号|商户号|银行卡号|选择的项目ID|缴费金额|系统跟踪号|检索参考号|受理机构代码|发送机构代码|清算日期|本地交易日期|本地交易时间|手机号|
	 */
	// 冲正：POSManager/remedy
	/**
	 * |终端号|商户号|银行卡号|选择的项目ID|缴费金额|系统跟踪号|检索参考号|受理机构代码|发送机构代码|清算日期|本地交易日期|本地交易时间|
	 */
	static String down = "|00000001|821520149000000|";

	public static void main(String[] args) {
		HttpResponse response = HttpRequest.post("localhost:8080/gzUnionpayWeb/POSManager/pay").body(down).send();
		response.bodyText();
		System.out.println("response:" + response.bodyText());
	}
}
// |00020005|821520149000002|622178981222211|11|7000|155327|141217155327|00167000|D0000170|1217|1219|155327|15281055731|0001|
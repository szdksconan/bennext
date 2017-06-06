package com.xsscd.controller;

import java.io.BufferedReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.service.PrCallService;
import com.xsscd.util.DesUtil;
import com.xsscd.util.RSACoder;

@ControllerBind(controllerKey = "/PrServiceManager")
public class PrServiceController extends Controller {
	private static Logger logger = LoggerFactory.getLogger(Class.class);
	private static PrCallService prms = new PrCallService();
	private static ConcurrentMap<String, String> PRIVATE_KEYS;	

	//初始化商户参数
	static{
		//私钥
		PRIVATE_KEYS = new ConcurrentHashMap<String, String>();
		PRIVATE_KEYS.put("800085100100001", "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAs3ffK8yLTN/YPIjXjbyGkPU0d+PvHd/aDBFpvSRYyLHxto81WQ8HoTkBqbli3ZiNh0P7yu1ro4V7SISxtbt33QIDAQABAkEArEkNIvzPVVMmQKaVV2l+PTZH0jgsWp+EsUUeHwb/fFlLMGebL+oGSMoQbow+0d23GXHAaktP/+kAXfAz0faWOQIhANsU8DIFr0dPFhzlm8C/ZWT3lz2U+uHO6c+vC7DdZXFXAiEA0bYHOyeq47Oz1hMHMORouC1191DHohfjx+RO6gAx2+sCIFCR5LwgxxNsjG9YKe269ekp/R8SuSluVSKh96S152bFAiEAyp6+o9gy0+i4FFm0M/gsee9qsDc7e5DrgyJDYPIpOlsCIQCDizn+1ICX4dXv4AMYbne2PRnSkgd5riGjGZIozZi0RA==");
		PRIVATE_KEYS.put("800085100100002", "14ee22eaba297944c96afdbe5b16c65b");		
	}

	private String checkMac(String[] params) throws Exception{
		logger.info("----------------- CHECK MAC -----------------");
		String reqData="";
		//根据商户号取出私钥，解密报文体
		String privateKey = PRIVATE_KEYS.get(params[1]);
		//解密
		BASE64Decoder decoder=new BASE64Decoder();
		BASE64Encoder encoder=new BASE64Encoder();
		byte[] desKey = RSACoder.decryptByPrivateKey(decoder.decodeBuffer(params[2]),decoder.decodeBuffer(privateKey));
		reqData = new String(DesUtil.decrypt(decoder.decodeBuffer(params[3]),desKey),"utf-8");
		logger.info("----------------- RCV DATA -----------------");
		logger.info(reqData);
		return reqData+"|"+encoder.encodeBuffer(desKey);
	}

	//查询持卡人卡权益信息列表
	public void findUserCardRight() {
		logger.info("----------------- FIND USER CARD RIGHT -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}	
			String[] arrayParams = reqData.split("\\|", -1);
			Record rs = new Record();
			rs.set("TermCode", tmps[0]);
			rs.set("MerchantCode", tmps[1]);
			rs.set("CardNum", arrayParams[0]);
			BASE64Decoder decoder=new BASE64Decoder();
			BASE64Encoder encoder=new BASE64Encoder();
			renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.findCardRightByCardNum(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));	
		}catch (Exception e) {
			renderText("|99|获取卡权益失败|");
			logger.error("获取卡权益失败", e);
		}
	}

	//查询权益可受理商户
	public void findMerCardRight() {
		logger.info("----------------- FIND MER CARD RIGHT -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}			
			String[] arrayParams = reqData.split("\\|", -1);
			Record rs = new Record();
			rs.set("TermCode", tmps[0]);
			rs.set("MerchantCode", tmps[1]);
			rs.set("RightID", arrayParams[0]);
			BASE64Decoder decoder=new BASE64Decoder();
			BASE64Encoder encoder=new BASE64Encoder();
			renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.findMerCardRight(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));
		}catch (Exception e) {
			renderText("|99|获取卡权益失败|");
			logger.error("获取卡权益失败", e);
		}
	}	

	//权益生成
	public void geneCardRight() {
		logger.info("----------------- GENE CARD RIGHT -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}
			String[] arrayParams = reqData.split("\\|", -1);
			Record rs = new Record();
			rs.set("TermCode", tmps[0]);
			rs.set("MerchantCode", tmps[1]);
			rs.set("CardNum", arrayParams[0]);
			rs.set("HolderName", arrayParams[1]);	
			rs.set("Phone", arrayParams[2]);			

			BASE64Decoder decoder=new BASE64Decoder();
			BASE64Encoder encoder=new BASE64Encoder();
			renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.geneCardRight(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));			
		}catch (Exception e) {
			renderText("|99|参数获取失败|");
			logger.error("参数获取失败", e);
		}
	}

	//权益使用预提交
	public void preConsume() {
		logger.info("----------------- PRE CONSUME -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}
			String[] arrayParams = reqData.split("\\|", -1);
			for(int i=0;i<arrayParams.length;i++){
				System.out.println("arr["+i+"]"+arrayParams[i]);
			}
			
			Record rs = new Record();
			if (arrayParams.length == 13) {
				rs.set("TermCode", tmps[0]);
				rs.set("MerchantCode", tmps[1]);
				rs.set("CardNum", arrayParams[0]);
				rs.set("RightID", arrayParams[1]);
				rs.set("Amount", arrayParams[2]);
				rs.set("SysTrackNum", arrayParams[3]);
				rs.set("RetRefNum", arrayParams[4]);// Retrieve reference number
				rs.set("AcceptCode", arrayParams[5]);// An accepting institution code
				rs.set("SenderCode", arrayParams[6]);
				rs.set("LiquiDate", arrayParams[7]);// The date of liquidation
				rs.set("LocalTransDate", arrayParams[8]);// The local transaction date
				rs.set("LocalTransTime", arrayParams[9]);
				rs.set("Phone", arrayParams[10]);
				rs.set("Version", arrayParams[11]);
				//调用并返回
				BASE64Decoder decoder=new BASE64Decoder();
				BASE64Encoder encoder=new BASE64Encoder();
				renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.preConsume(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));				
			} else {
				renderText("|99|非法参数|");
			}
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			logger.error("参数获取失败", e);
		}
	}

	//权益使用预提交回滚
	public void rollbackConsume() {
		logger.info("----------------- ROLLBACK CONSUME -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}
			String[] arrayParams = reqData.split("\\|", -1);
			Record rs = new Record();
			rs.set("CardNum", arrayParams[0]);
			rs.set("RightID", arrayParams[1]);			
			rs.set("SysTrackNum", arrayParams[2]);
			rs.set("RetRefNum", arrayParams[3]);// Retrieve reference number

			BASE64Decoder decoder=new BASE64Decoder();
			BASE64Encoder encoder=new BASE64Encoder();			
			renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.rollbackConsume(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));			
		} catch (Exception e) {
			renderText("|99|参数获取失败|");
			logger.error("参数获取失败", e);
		}		
	}	

	//权益使用提交
	public void submitConsume() {
		logger.info("----------------- SUBMIT CONSUME -----------------");
		try {
			HttpServletRequest request = getRequest();
			BufferedReader in = request.getReader();
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			String[] tmps = buffer.toString().split("\\|", -1);
			String reqData = checkMac(tmps);
			if(StringUtils.isEmpty(reqData)){
				renderText("|11|请求参数处理异常|");
				return;
			}
			String[] arrayParams = reqData.split("\\|", -1);
			Record rs = new Record();
			rs.set("CardNum", arrayParams[0]);
			rs.set("RightID", arrayParams[1]);			
			rs.set("SysTrackNum", arrayParams[2]);
			rs.set("RetRefNum", arrayParams[3]);// Retrieve reference number

			BASE64Decoder decoder=new BASE64Decoder();
			BASE64Encoder encoder=new BASE64Encoder();				
			renderText("|00|"+encoder.encodeBuffer(DesUtil.encrypt(prms.submitConsume(rs).getBytes("utf-8"),decoder.decodeBuffer(arrayParams[arrayParams.length-1]))));			
		} catch (Exception e) {
			renderText("|99|权益使用提交失败|");
			logger.error("权益使用提交失败", e);
		}		
	}
}

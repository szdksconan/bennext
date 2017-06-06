package com.xsscd.util;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.dao.MsgDao;
import com.xsscd.dao.POSManagerDao;

public class MsgUtil {

	private static String key = "C9FA1B1C65D0776184Q955A48EBC5A9F";
	private static String payMsgModel1 = "尊敬的cardrank持卡人，您尾号为cardnum卡本次享受display服务，本年度该权益剩余count次。";
	private static String payMsgModel2 = "尊敬的cardrank持卡人，您尾号为cardnum卡本次享受display服务，关注62字头银联cardrank，更多惊喜等着您。";
	private static String payMsgModel3 = "您尾号为cardnum卡本次享受display服务。如需继续使用该权益，请前往银行申领卡号以62开头的银标白金卡，可免费享受30次/年";
	private static String payMsgModel4 = "您尾号为cardnum卡display体验次数已用完。如需使用该权益，请前往银行申领卡号以62开头的银标白金卡，可免费享受30次/年";
	private static POSManagerDao pmdao = new POSManagerDao();

	public static void sendMsgOnPaySuccess(String phone, String cardNum, String rightID)
			throws NoSuchAlgorithmException, IOException {
		Record cardinfo = pmdao.findCardInfoByCardNum(cardNum);
		int cardType = cardinfo.getInt("CardTypeID");
		Record rd = pmdao.findInfoFromCardRight(cardNum, rightID);
		String rightName = rd.getStr("Display");
		int count = rd.getInt("Count");
		String model = "";
		if (cardType == -1) {
			model = payMsgModel3;
		} else {
			model = count < 10000 ? payMsgModel1 : payMsgModel2;
		}

		String cardrank = cardType == -1 ? "银联卡" : pmdao.findCardTypeDescription(cardType);
		String last4num = cardNum.substring(cardNum.length() - 4, cardNum.length());
		final String accessId = "QYPT";
		final String receiver = phone;
		String tmpcontent = model.replace("cardrank", cardrank).replace("cardnum", last4num)
				.replace("display", rightName).replace("count", count + "");
		final String content = URLEncoder.encode(tmpcontent, "utf-8");
		String tmp = MD5Util.getEncryptString(accessId + key) + MD5Util.getEncryptString(receiver + key)
				+ MD5Util.getEncryptString(content + key);
		final String sign = MD5Util.getEncryptString(tmp);
		String path = PathKit.getRootClassPath() + File.separator + "mysql.properties";
		PropKit.use(new File(path));
		String url = PropKit.get("msgUrl");
		HttpResponse response = HttpRequest.post(url)
				.form("accessId", accessId, "receiver", receiver, "content", content, "sign", sign).send();
		response.bodyText();
		MsgDao.dao.addMsg(phone, tmpcontent, "POS消费短信");
		System.out.println("response:" + response.bodyText());
	}

	public static void sendMsgOnPayFailure(String phone, String cardNum, String rightID)
			throws NoSuchAlgorithmException, IOException {
		Record cardinfo = pmdao.findCardInfoByCardNum(cardNum);
		int cardType = cardinfo.getInt("CardTypeID");
		Record rd = pmdao.findInfoFromCardRight(cardNum, rightID);
		String rightName = rd.getStr("Display");
		int count = rd.getInt("Count");
		String model = payMsgModel4;
		if (cardType == -1) {
			model = payMsgModel4;
		}

		String cardrank = cardType == -1 ? "银联卡" : pmdao.findCardTypeDescription(cardType);
		String last4num = cardNum.substring(cardNum.length() - 4, cardNum.length());
		final String accessId = "QYPT";
		final String receiver = phone;
		String tmpcontent = model.replace("cardrank", cardrank).replace("cardnum", last4num)
				.replace("display", rightName).replace("count", count + "");
		final String content = URLEncoder.encode(tmpcontent, "utf-8");
		String tmp = MD5Util.getEncryptString(accessId + key) + MD5Util.getEncryptString(receiver + key)
				+ MD5Util.getEncryptString(content + key);
		final String sign = MD5Util.getEncryptString(tmp);
		String path = PathKit.getRootClassPath() + File.separator + "mysql.properties";
		PropKit.use(new File(path));
		String url = PropKit.get("msgUrl");
		HttpResponse response = HttpRequest.post(url)
				.form("accessId", accessId, "receiver", receiver, "content", content, "sign", sign).send();
		response.bodyText();
		MsgDao.dao.addMsg(phone, tmpcontent, "POS消费短信");
		System.out.println("response:" + response.bodyText());
	}
}

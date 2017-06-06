package com.xsscd.util;

import java.io.File;
import java.net.URLEncoder;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.xsscd.base.BaseService;
import com.xsscd.dao.MsgDao;

/**
 * 短信工具
 * 
 * @author zengcy
 * 
 */
public class MessageUtil {
	public final static MessageUtil msg = new MessageUtil();
	private final static String key = "C9FA1B1C65D0776184Q955A48EBC5A9F";
	private final static String accessId = "QYPT";

	public boolean send(String phone, String content) {
		boolean success = true;
		try {
			String tempcontent = URLEncoder.encode(content, "utf-8");

			String tmp = MD5Util.getEncryptString(accessId + key) + MD5Util.getEncryptString(phone + key)
					+ MD5Util.getEncryptString(tempcontent + key);
			final String sign = MD5Util.getEncryptString(tmp);
			String path = PathKit.getRootClassPath() + File.separator + "mysql.properties";
			PropKit.use(new File(path));
			String url = PropKit.get("msgUrl");
			HttpResponse response = HttpRequest.post(url)
					.form("accessId", accessId, "receiver", phone, "content", tempcontent, "sign", sign).send();
			BaseService.log.info("短信发送结果:" + response.bodyText());
			MsgDao.dao.addMsg(phone, content, "POS消费短信");
		} catch (Exception e) {
			success = false;
			BaseService.log.error("短信发送失败" + e);
		} finally {
			return success;
		}

	}

}

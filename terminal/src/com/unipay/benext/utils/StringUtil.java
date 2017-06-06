package com.unipay.benext.utils;

public class StringUtil {

	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	
	/**
	 * 获取扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtention(final String fileName) {
		int pos = fileName.lastIndexOf(".");
		String name = fileName.substring(pos);
		return name;
	}

}

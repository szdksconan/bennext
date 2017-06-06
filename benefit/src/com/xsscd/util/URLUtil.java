package com.xsscd.util;

import jodd.util.URLDecoder;

public class URLUtil {

	public static String decode(String src, String encoding) {
		if (src == null) {
			return null;
		} else {
			return URLDecoder.decode(src, encoding);
		}
	}

	public static String decodeUTF8(String src) {
		if (src == null) {
			return null;
		} else {
			return URLDecoder.decode(src, "utf-8");
		}
	}
}

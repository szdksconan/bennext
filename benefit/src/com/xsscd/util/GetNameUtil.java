package com.xsscd.util;

import java.lang.reflect.Field;

public class GetNameUtil {
	/**
	 * 根据列名获取属性名
	 * 
	 * @param colName
	 * @return
	 */
	public static String getFieldName(String colName) {
		String[] colArray = colName.toLowerCase().split("_");
		StringBuilder fieldName = new StringBuilder();
		for (int i = 0; i < colArray.length; i++) {
			if (i == 0) {
				fieldName.append(colArray[i]);
			} else {
				fieldName.append(colArray[i].substring(0, 1).toUpperCase() + colArray[i].substring(1));
			}
		}
		return fieldName.toString();
	}

	/**
	 * 根据属性名获取设定器的名字
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getSetter(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * 根据属性名获取访问器的名字
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getGetter(Field field) {
		return ((field.getType() == boolean.class) ? "is" : "get") + field.getName().substring(0, 1).toUpperCase()
				+ field.getName().substring(1);
	}

	/**
	 * 根据 类名或属性名获取 表名或者列名
	 * 
	 * @param name
	 * @return emp crmRoleRight
	 */
	public static String getTabOrColName(String name) {
		name = name.replace("Vo", "");
		StringBuffer tabOrColName = new StringBuffer();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (c >= 'A' && c <= 'Z' && i != 0) {
				tabOrColName.append("_" + c);
			} else {
				tabOrColName.append(c);
			}
		}
		return tabOrColName.toString().toLowerCase();
	}
}

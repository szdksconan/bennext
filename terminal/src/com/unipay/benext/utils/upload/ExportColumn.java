package com.unipay.benext.utils.upload;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 保存需要导出列的信息
 * @author perfect
 *
 * @param <T>
 */
public class ExportColumn<T> {	
	
	private Class clzz;
	private T obj;
	private Field field;
	private Method method;
	private ExcelColumnMapping ecm;
	private String columnName;
	private String content;
	
	public ExportColumn(T t, Field field) {
		this.init(t, field);
	}
	
	/**
	 * 初始化方法
	 * @param clzz
	 * @param field
	 */
	private void init(T t, Field field) {
		this.clzz = t.getClass();
		this.obj = t;
		try {
			//获取数据对象的get方法	
			this.method = this.getMethod(clzz, field);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		//获取ExcelColumnMapping
		if (field.isAnnotationPresent(ExcelColumnMapping.class)) {
			ecm = field.getAnnotation(ExcelColumnMapping.class);
		}
	}
	
	/**
	 * 获取一个属性的get方法
	 * @param clzz
	 * @param field
	 * @return Mehotd
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Method getMethod(Class clzz, Field field) throws SecurityException, NoSuchMethodException {
		Method method = null;
		String fieldName = field.getName();
		String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		method = clzz.getMethod(methodName);		
		return method;
	}
	
	/**
	 * 获取列名
	 * @return String
	 */
	public String getColumnName() {
		return ecm.columnName();
	}	
	
	/**
	 * 获取内容
	 * @return String
	 */
	public String getContent() {
		String content = null;
		try {
			content = method.invoke(obj).toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			content = "";
		}
		return content;
	}

}

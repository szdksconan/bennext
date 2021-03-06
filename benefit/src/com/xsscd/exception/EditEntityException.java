package com.xsscd.exception;

import java.util.HashMap;
import java.util.Map;

public class EditEntityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 带的参数
	 */
	private Map<String, Object> map = new HashMap<String, Object>();

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public EditEntityException() {
	}

	public EditEntityException(String errorInfo) {
		super(errorInfo);
		this.map.put("errorInfo", errorInfo);
	}
}

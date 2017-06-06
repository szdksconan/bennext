package com.xsscd.vo;

import java.util.List;


/**
 * 用于返回给前台的通用结构
 * 带分页的请返回 所有 属性
 * 普通请求 返回 isSuccess message
 * 单个信息查询 返回  isSuccess message itemList(单个信息放到第一个位置)
 * @author zengcy
 *
 */
public class ResultVo {
	private Boolean isSuccess;
	private String message;
	private List<? extends Object> itemList;
	private Integer count;
	
	public ResultVo() {
	}
	

	public ResultVo(Boolean isSuccess, String message) {
		super();
		this.isSuccess = isSuccess;
		this.message = message;
	}
	public ResultVo(Boolean isSuccess, String message, List<? extends Object> itemList,
			Integer count) {
		super();
		this.isSuccess = isSuccess;
		this.message = message;
		this.itemList = itemList;
		this.count = count;
	}


	public Boolean getIsSuccess() {
		return isSuccess;
	}


	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public List<? extends Object> getItemList() {
		return itemList;
	}


	public void setItemList(List<? extends Object> itemList) {
		this.itemList = itemList;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * setter and getter
	 */
	
	
}

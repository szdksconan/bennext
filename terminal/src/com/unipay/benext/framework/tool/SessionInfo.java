package com.unipay.benext.framework.tool;

import java.util.List;

public class SessionInfo implements java.io.Serializable {

	private Long id;// 用户ID
	private String loginname;// 登录名
	private String name;// 姓名
	private String ip;// 用户IP
	private Integer dataRightType;	//数据访问权限  0：本人;1：本部门;2：本部门及下级部门;3：全公司

	private List<String> accessList;// 用户可以访问的资源地址列表
	
	private List<String> accessAllList;
	
	private String deviceToken;//移动设备标识号
	
	private String deviceType;//设备类型ios   android
	
	private boolean isSuperAadmin;//是否超级管理员
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	

	public List<String> getAccessList() {
		return accessList;
	}

	public void setAccessList(List<String> accessList) {
		this.accessList = accessList;
	}

	public List<String> getAccessAllList() {
		return accessAllList;
	}

	public void setAccessAllList(List<String> accessAllList) {
		this.accessAllList = accessAllList;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Integer getDataRightType() {
		return dataRightType;
	}

	public void setDataRightType(Integer dataRightType) {
		this.dataRightType = dataRightType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isSuperAadmin() {
		return isSuperAadmin;
	}

	public void setSuperAadmin(boolean isSuperAadmin) {
		this.isSuperAadmin = isSuperAadmin;
	}

	
	

}

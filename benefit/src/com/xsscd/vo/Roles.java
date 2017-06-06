package com.xsscd.vo;

public enum Roles {
	systemMgr(0,"系统管理员"),
	serviceSuperMgr(1,"营运方超级管理员"),
	serviceMgr(2,"营运方管理员"),
	supplyMgr(3,"供应方管理员"),
	supplyOperator(4,"供应方操作员"),
	cardUser(5,"持卡人");
	private Roles(Integer roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	public final Integer roleId;
	public final String roleName;
	@Override
	public String toString() {
		return this.roleId+"";
	}
}

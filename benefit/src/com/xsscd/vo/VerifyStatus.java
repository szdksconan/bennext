package com.xsscd.vo;

public enum VerifyStatus {
	noAuditBackUser(100,"未审核"),
	auditPassBackUser(101,"已通过"),
	auditNoPassBackUser(102,"未通过"),
	stopUsingBackUser(103,"停用"),
	valid(110,"有效"),
	invalid(111,"无效"),
	noAuditRightRule(120,"未审核"),
	auditPassRightRule(121,"通过(分配中)"),
	auditNoPassRightRule(122,"未通过"),
	stopUsingRightRule(123,"停用(停用中)"),
	assignedRightRule(124,"已分配(部分卡)"),
	stopUsingCompleteRightRule(125,"已停用");
	VerifyStatus(Integer code,String description){
		this.code=code;
		this.description=description;
	}
	public final Integer code;
	public final String description;
	@Override
	public String toString() {
		return this.code+"";
	}
	
}

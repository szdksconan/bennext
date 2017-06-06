package com.unipay.benext.pageModel.auth;

import java.util.Date;

public class User implements java.io.Serializable {

	private static final long serialVersionUID = 6468635653496314973L;
	
	private Long id;
	private String loginName; // 登录名
	private String loginPwd; // 登录密码
	private String name; // 姓名
	private String email; //邮箱
	private String userNumber; //员工号
	private String phoneNumber; //手机号
	private Integer sex; // 性别
	private String position; // 职位
	private Date entryDate; // 入职时间
	private Integer isCheckWork; // 是否考勤
	private String creator; //创建人
	private Date createTime; // 创建时间
	private Integer isDefault; // 是否默认
	private Integer status; // 状态
	private Integer isEnabled; //是否启用 0启用 1停用
	private String description; //描述

	private Long roleId;  //角色id
	private String roleName; //角色名称
	private Integer dataRightType; //数据权限类型
	
	private Long attSystemId; //考勤制度id
	private String attSystemName; //考勤制度名称
	
	private String workAreaIds; //工作区域Id
	private String workAreaNames; //工作区域名称
	
	private Date createTimeStart; 
	private Date createTimeEnd; 
	
	private String verify; //验证码

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getIsCheckWork() {
		return isCheckWork;
	}

	public void setIsCheckWork(Integer isCheckWork) {
		this.isCheckWork = isCheckWork;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getDataRightType() {
		return dataRightType;
	}

	public void setDataRightType(Integer dataRightType) {
		this.dataRightType = dataRightType;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public Long getAttSystemId() {
		return attSystemId;
	}

	public void setAttSystemId(Long attSystemId) {
		this.attSystemId = attSystemId;
	}

	public String getAttSystemName() {
		return attSystemName;
	}

	public void setAttSystemName(String attSystemName) {
		this.attSystemName = attSystemName;
	}

	public String getWorkAreaIds() {
		return workAreaIds;
	}

	public void setWorkAreaIds(String workAreaIds) {
		this.workAreaIds = workAreaIds;
	}

	public String getWorkAreaNames() {
		return workAreaNames;
	}

	public void setWorkAreaNames(String workAreaNames) {
		this.workAreaNames = workAreaNames;
	}
	
}
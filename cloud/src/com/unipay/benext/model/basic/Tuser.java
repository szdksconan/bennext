package com.unipay.benext.model.basic;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ts_app_user")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Tuser implements java.io.Serializable {
	
	private static final long serialVersionUID = 6238285790991537093L;
	
	protected Long id;
	private String loginName; // 登录名
	private String loginPwd; // 登录密码
	private String name; // 姓名
	private String email; //邮箱
	private String userNumber; //员工号
	private String phoneNumber; //手机号
	private Integer sex; // 性别
	private String position; // 职位
	private Date entryDate; // 入职时间
	private Integer isCheckWork; // 是否考勤 0是 1否
	private String creator; //创建人
	private Date createTime; // 创建时间
	private Integer isDefault; // 是否默认 0默认 1非默认
	private Integer status; // 状态
	private Integer isEnabled; //是否启用 0启用 1停用
	private String description; //描述

	public Tuser() {
		super();
	}

	public Tuser(String loginName, String loginPwd, String name, String email, String userNumber,
                 String phoneNumber, Integer sex, String position, Date entryDate, Integer isCheckWork, Integer isEnabled,
                 String creator, Date createTime, Integer isDefault, Integer status, String description) {
		super();
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.name = name;
		this.email = email;
		this.userNumber = userNumber;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.position = position;
		this.entryDate = entryDate;
		this.isCheckWork = isCheckWork;
		this.creator = creator;
		this.createTime = createTime;
		this.isDefault = isDefault;
		this.isEnabled = isEnabled;
		this.status = status;
		this.description = description;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@Column(name="login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name="login_pwd")
	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	public Integer getSex() {
		return sex;
	}
	
	public void setSex(Integer sex){
		this.sex = sex;
	}

	@NotBlank
	public String getPosition(){
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	@NotBlank
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotBlank
	@Column(name="user_number")
	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	@NotBlank
	@Column(name="phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date", length = 19)
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@NotNull
	@Column(name="is_check_work")
	public Integer getIsCheckWork() {
		return isCheckWork;
	}

	public void setIsCheckWork(Integer isCheckWork) {
		this.isCheckWork = isCheckWork;
	}

	@NotBlank
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@NotNull
	@Column(name="is_default")
	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@NotNull
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@NotNull
	@Column(name="is_enabled")
	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
}
package com.xsscd.service;

import com.xsscd.dao.UserDao;
import com.xsscd.entity.User;

public class PlatformManagerService {
	private UserDao udao = new UserDao();
	public String login(String uName,String psw){
		try{
			if(udao.login(uName, psw) != null){
				return "true";
			}else{
				return "false";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "false";
		}
	}
	public String register(User regUser) {
		try {
			if(null != udao.findUserBySingleCondition("Uname",regUser.getuName())){
				return "false,该用户名已被注册！";
			}else if(null != udao.findUserBySingleCondition("Phone",regUser.getPhone())){
				return "false,该手机号已被注册！";
			}else if(null != udao.findUserBySingleCondition("Email",regUser.getEmail())){
				return "false,该邮箱已被注册！";
			}else{
				udao.insert(regUser);
				return "true,用户注册成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "false,用户注册失败！";
		}
	}
	public String validateUserName(String uName) {
		try {
			if(null != udao.findUserBySingleCondition("Uname",uName)){
				return "false,用户名已存在！";
			}else{
				return "true,该用户名可以使用！";
			}
		} catch (Exception e) {
			return "false,系统出错，请重试！";
		}
	}
}

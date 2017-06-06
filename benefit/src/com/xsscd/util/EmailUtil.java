package com.xsscd.util;

import java.io.File;

import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import jodd.mail.att.FileAttachment;

public class EmailUtil {
	private final String username;
	private final String password;
	private final String displayname;
	private String smtp="smtp.163.com";
	private boolean isssl=false;
	//默认去配置文件找账号密码及显示名
	public EmailUtil(){
		this.username="xsscd11@163.com";
		this.password="xsscd121";
		this.displayname="贵州银联系统邮件";
	}
	public EmailUtil(String username,String password,String displayname){
		this.username=username;
		this.password=password;
		this.displayname=displayname;
	}
	public EmailUtil(String username,String password,String displayname,String smtp,boolean isssl){
		this.username=username;
		this.password=password;
		this.displayname=displayname;
		this.smtp=smtp;
		this.isssl=isssl;
	}
	/**
	 * 普通邮件
	 * @param to
	 * @param title
	 * @param message
	 */
	public void sendEmail(String to,String title,String message){
			//该问题由于不知名Bug导致，发送邮件时，会默认去掉displayname的最后一个字符
			String name=this.displayname+"啊";
			Email email = Email.create()
			        .from(name+"<"+this.username+">")
			        .to(to)
			        .subject(title)
			        .addText(message);
			send(email);
	}
	/**
	 * 带附件的邮件
	 * @param to
	 * @param title
	 * @param message
	 * @param filepath
	 * @param filename
	 */
	public void sendEmailwithfile(String to,String title,String message,String filepath,String filename){
			//该问题由于不知名Bug导致，发送邮件时，会默认去掉displayname的最后一个字符
			String name=this.displayname+"啊";
			Email email = Email.create()
			        .from(name+"<"+this.username+">")
			        .to(to)
			        .subject(title)
			        .addText(message);
			EmailAttachment attachment = new FileAttachment(new File(filepath), filename, "image/jpeg");
			email.attach(attachment);
	}
	private void send(Email email){
		SendMailSession mailSession;
		if(this.isssl)
			mailSession = new SmtpSslServer(this.smtp,this.username, this.password).createSession();
		else
			mailSession = new SmtpServer(this.smtp,this.username, this.password).createSession();
		mailSession.open();
		mailSession.sendMail(email);
		mailSession.close();
	}
}

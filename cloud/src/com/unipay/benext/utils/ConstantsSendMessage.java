package com.unipay.benext.utils;

/**
 * 消息推送工具类
 * @author Administrator
 *
 */
public class ConstantsSendMessage {
	//public static ResourceBundle res=ResourceBundle.getBundle("/conf/config");
	//public static String fname=res.getString("msgurl");
	//成都测试消息服务部署地址
//	public static final String SEND_MESSAGE_URL= "http://192.168.31.170:8090/CooperLinkMPM/webservice/";
	//北京内网测试消息服务部署地址
	//public static final String SEND_MESSAGE_URL= "http://192.168.0.45:8090/CooperLinkMPM/webservice/";
	//北京外网测试消息服务部署地址
	public static final String SEND_MESSAGE_URL= "http://219.142.192.242:48765/CooperLinkMPM/webservice/";

	
	/**
	 * 注册应用名称
	 */
	public static final String AppCode= "yht";
	
	/**
	 * 主题名称
	 */
	public static final String TopicCode= "mac00eebd82e84f";
	
	/**
	 * 应用注册接口
	 */
	public static final String SEND_MESSAGE_RegisterApp ="registerApp";
	/**
	 * 创建主题
	 */
	public static final String SEND_MESSAGE_CreateTopic ="createTopic";
	/**
	 * 订阅主题
	 */
	public static final String SEND_MESSAGE_SubscribeTopic ="subscribeTopic";
	/**
	 * 发送消息
	 */
	public static final String SEND_MESSAGE_PushMessage ="pushMessage";
	
	
	
	
}

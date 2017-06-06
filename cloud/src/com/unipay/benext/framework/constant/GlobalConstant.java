package com.unipay.benext.framework.constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalConstant {

	public static final String SESSION_INFO = "sessionInfo";
	public static final String REQUEST_MD5_CHAR = "yht2015";

	public static final Integer ENABLE = 0; // 启用
	public static final Integer DISABLE = 1; // 禁用
	
	public static final Integer DEFAULT = 0; // 默认
	public static final Integer NOT_DEFAULT = 1; // 非默认
	
	public static final Integer CHECKWORK_TRUE = 0; //是   考勤
	public static final Integer CHECKWORK_FALSE = 1; //否  考勤
	
	public static final Integer DATERIGHT_MYSELF = 0; //本人
	public static final Integer DATERIGHT_DEPARTMENT = 1; //部门
	public static final Integer DATERIGHT_F_DEPARTMENT = 2; //部门及下级部门
	public static final Integer DATERIGHT_COMPANY = 3;//全公司
	
	public static final Integer DATECLASS_TEXT = 0; //文本
	public static final Integer DATECLASS_SINGLE = 1; //单选
	public static final Integer DATECLASS_COMPLEX = 2; //多选
	public static final Integer DATECLASS_TEXTAREA = 3; //文本域
	public static final Integer DATECLASS_DATE = 4; //日期
	public static final Integer DATECLASS_PICTURE = 5; //图片
	public static final Integer DATECLASS_DIGITAL = 6; //数字
	public static final Integer DATECLASS_SOURCE = 7; //数据源
	public static final Integer DATECLASS_AUTONO = 8; //自动编号
	
	public static final Integer LEAVE_TYPE_SICK = 0; //病假
	public static final Integer LEAVE_TYPE_ABSENCE = 1; //事假
	public static final Integer LEAVE_TYPE_MARRIAGE = 2;  //婚假
	public static final Integer LEAVE_TYPE_MATERNITY = 3; //产假
	public static final Integer LEAVE_TYPE_ANNUAL = 4; //年假
	public static final Integer LEAVE_TYPE_OFF = 5; //调休
	public static final Integer LEAVE_TYPE_OTHER = 6; //其他
	
	public static final Integer TASKSTATUS_WORK = 0; //待完成
	public static final Integer TASKSTATUS_DONE = 1; //已完成
	public static final Integer TASKSTATUS_REVOKE = 2; //已撤销
	
	public static final Integer REMIND_NO = 0; //不提醒
	public static final Integer REMIND_HALF = 1; //提前半小时
	public static final Integer REMIND_ONE = 2; //提前1天
	
	
	/** 提醒状态---待提醒*/
	public static final Integer REMIND_STATUS_WAIT = 0;//待提醒
	/** 提醒状态---已提醒(未读)*/
	public static final Integer REMIND_STATUS_NOREAD = 1;//已提醒(未读)
	/** 提醒状态---已读*/
	public static final Integer REMIND_STATUS_READED = 2;//已读
	
	/** 提醒重要程度 ---紧急*/
	public static final Integer REMIND_IMPORTANT_EMERGE = 0;//紧急
	/** 提醒重要程度 ---重要*/
	public static final Integer REMIND_IMPORTANT_INPORT = 1;//重要
	/** 提醒重要程度 ---一般*/
	public static final Integer REMIND_IMPORTANT_GENERAL = 2;//一般
	
	public static final Integer REMIND_TYPE_SLEF = 0;//提醒类型-----自有
	public static final Integer REMIND_TYPE_TASK = 1;//提醒类型-----任务提醒
	public static final Integer REMIND_TYPE_CALENDER = 2;//提醒类型-----日历提醒
	public static final Integer REMIND_TYPE_CHECK = 3;//提醒类型-----请假提醒
	
	
	/** 公告公示期类型 ---无截止日期*/
	public static final Integer NOTICE_TIME_NO = 0;//无截止日期
	/** 公告公示期类型 ---指定日期*/
	public static final Integer NOTICE_TIME_ONEDAY = 1;//指定日期
	/** 公告公示期 类型---指定天数*/
	public static final Integer NOTICE_TIME_DAYS = 2;//指定天数
	
	public static final Integer NOTICE_SENDRANGE_ALL = 0;//公告发送范围---全体
	public static final Integer NOTICE_SENDRANGE_CHOOSE = 0;//公告发送范围---选择对象
	
	public static final Integer NOTICE_ISTOP_YES = 0;//公告是否置顶---是
	public static final Integer NOTICE_ISTOP_NO = 1;//公告是否置顶---否
	
	public static final Integer NOTICE_STATUS_USE = 0;//公告状态---已发布
	public static final Integer NOTICE_STATUS_STOP = 1;//公告状态---草稿
	public static final Integer NOTICE_STATUS_INVALID = 2;//公告状态---删除
	
	public static final Integer EVENT_REMIND_ONTIME = 0;//工作日历提醒时间类型---准时提醒
	public static final Integer EVENT_REMIND_BF_5M = 1;//工作日历提醒时间类型---提前5分钟提醒
	public static final Integer EVENT_REMIND_BF_15M = 2;//工作日历提醒时间类型---提前15分钟提醒
	public static final Integer EVENT_REMIND_BF_ONEDAY = 3;//工作日历提醒时间类型---提前1天提醒
	public static final Integer EVENT_REMIND_BF_ONEWEEK = 4;//工作日历提醒时间类型---提前1周提醒
	public static final Integer EVENT_REMIND_BF_ONEMONTH = 5;//工作日历提醒时间类型---提前1月提醒
	public static final Integer EVENT_REMIND_SPECIAL_TIME = 6;//工作日历提醒时间类型---指定日期
	
	public static final Map sexlist = new HashMap(){{ put("0", "男");  put("1", "女");} };
	public static final Map statelist = new HashMap(){{ put("0", "启用");  put("1", "停用");} };
	public static final Map isOrNotlist = new HashMap(){{put("0","是");put("1","否");}};
	public static final Map datatypelist = new LinkedHashMap(){{put("0","本人");put("1","本部门");put("2","本部门及下级部门");put("3","全公司");}};
	public static final Map dataclasslist = new HashMap(){{put("0","文本");put("1","单选");put("2","多选");put("3","文本域");put("4","日期");put("5","图片");put("6","数字");put("7","数据源");put("8","自动编号");}};
	public static final String[] dataclassArr = new String[]{"文本","单选","多选","文本域","日期","图片","数字","数据源","自动编号"};
	public static final Map taskstatuslist = new HashMap(){{put("0","待完成");put("1","已完成");put("2","已撤销");}};
	public static final Map remindlist = new HashMap(){{put("0","不提醒");put("1","提前半小时");put("2","提前1天");put("3","指定时间");}};
	public static final Map checktypelist = new HashMap(){{put("0","我参与的");put("1","我发起的");put("2","下属参与的");}};
	public static final Map showaddressbooklist = new HashMap(){{ put("0", "可见");  put("1", "不可见");} };
	public static final Map dateformat = new HashMap(){{put("0","yyyy/MM/dd");put("1","yyyy-MM-dd");put("2","yyyyMMdd");put("3","yyyy/MM/dd HH:mm:ss");put("4","yyyy-MM-dd HH:mm:ss");put("5","yyyyMMdd HH:mm:ss");}};
	public static final Map productServiceType = new HashMap(){{put("0","未开始");put("1","服务中");put("2","已结束");}};
	public static final Map chargeNoticeStatus = new HashMap(){{put("0","未付款");put("1","已付款");put("2","已取消");}};
	public static final Map channelType = new HashMap(){{put("0","创投机构");put("1","孵化器");put("2","众创空间");put("3","创业服务合作伙伴");}};
	
	/** 提醒重要程度集合对象*/
	public static final Map remindImportantList = new HashMap(){{put("0","紧急");put("1","重要");put("2","一般");}};
	/** 公告公示期 类型集合对象*/
	public static final Map noticTimeTypeList = new HashMap(){{put("0","无截止日期");put("1","指定日期");put("2","指定天数");}};
	
	public static final Map noticSendRangeList = new HashMap(){{put("0","全体");put("1","选择对象");}};
	public static final Map noticIsTopList = new HashMap(){{put("0","是");put("1","否");}};
	public static final Map noticStatusList = new HashMap(){{put("0","已发布");put("1","草稿");}};
	public static final Map eventRemindList = new HashMap(){{put("0","准时提醒");put("1","提前5分钟提醒");put("2","提前15分钟提醒");put("3","提前1天提醒");put("4","提前一周");put("5","提前一个月");put("6","指定日期提醒");}};
	public static final Map leaveTypeList = new HashMap(){{put("0","病假");put("1","事假");put("2","婚假");put("3","产假");put("4","年假");put("5","调假");put("6","其他");}};
}

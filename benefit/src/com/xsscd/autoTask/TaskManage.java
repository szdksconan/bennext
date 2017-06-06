package com.xsscd.autoTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class TaskManage implements Runnable {
	public static TaskManage tm = new TaskManage();
	private final List<Timer> timerList = new ArrayList<Timer>();

	/**
	 * 开启任务线程
	 */
	public void startAllTask() {
		// 为任务管理开启新线程
		Thread timer = new Thread(this);
		timer.setName("任务管理timer的线程");
		timer.start();
	}

	/**
	 * 具体开始所有任务
	 */
	@Override
	public void run() {
		startRightRuleTask();
	}

	/**
	 * 停止所有任务
	 */
	public void stopAllTask() {
		// 在这里关闭监听器，所以在这里销毁定时器。
		for (Timer timer : this.timerList) {
			timer.cancel();
		}
	}

	/**
	 * 权益规则任务
	 */
	private void startRightRuleTask() {
		Timer timer = new Timer();
		this.timerList.add(timer);
		// 指定任务在晚上1点00分执行：
		Calendar calendar = Calendar.getInstance();
		// 因为是凌晨，当前日期加一天
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		// 设置时间
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();// 1000 * 60 * 60 * 24
		timer.scheduleAtFixedRate(new RightRuleTask(), time, 1000 * 60 * 60 * 24);
	}
}

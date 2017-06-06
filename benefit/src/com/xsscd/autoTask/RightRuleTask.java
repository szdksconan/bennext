package com.xsscd.autoTask;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.RightRuleDao;
import com.xsscd.vo.VerifyStatus;

/**
 * 权益规则的任务工作
 * 
 * @author zengcy
 * 
 */
public class RightRuleTask extends TimerTask {
	@Override
	public void run() {
		this.allocationCardRight();
		this.deleteCardRight();
	}

	/**
	 * 分配卡权益
	 */
	private void allocationCardRight() {
		BaseService.log.info("开始权益规则分配任务");
		// 主动给rightcounttemp表插入满足条件的rightcount数据
		RightRuleDao.dao.addTempforAllAuditPass();
		// 1查询所有需要分配权益的数据(通过和已分配都要查询（防止新卡用户无权益）)-rightid rank
		List<Record> list = RightRuleDao.dao.findTemp(VerifyStatus.auditPassRightRule.code,
				VerifyStatus.assignedRightRule.code);
		// 2循环处理所有记录，每个记录一个事务
		for (Record rc : list) {
			final Integer RightID = rc.getInt("RightID"), Rank = rc.getInt("Rank"), Count = rc.getInt("Count");
			Db.tx(new IAtom() {
				@SuppressWarnings("finally")
				@Override
				public boolean run() throws SQLException {
					boolean isSuccess = true;
					try {
						/**
						 * 为每个权益规则对应的所有卡分配权益
						 */
						// 1封装批量添加的sql进行执行，忽略已经分配的权益
						isSuccess = RightRuleDao.dao.allocationRightCount(RightID, Rank, Count);
						// （防止新卡用户无权益这里不删除） 2删除对应的rightcounttemp数据
						// isSuccess = RightRuleDao.dao.deleteRightCountTemp(RightID, Rank);
						// 3更改rightcount对应数据的状态
						isSuccess = RightRuleDao.dao.editRightCountVID(RightID, Rank,
								VerifyStatus.assignedRightRule.code);
					} catch (Exception e) {
						isSuccess = false;
						BaseService.log.error("分配权益规则 rightId:" + RightID + " Rank:" + Rank + e);
					} finally {
						return isSuccess;
					}
				}
			});
		}
		BaseService.log.info("结束权益规则分配任务,共处理" + list.size() + "个权益规则");
	}

	/**
	 * 删除卡权益
	 */
	private void deleteCardRight() {
		BaseService.log.info("开始卡权益删除任务");
		// 1查询所有需要删除卡权益的数据-rightid rank
		List<Record> list = RightRuleDao.dao.findTemp(VerifyStatus.stopUsingRightRule.code);
		// 2循环处理所有记录，每个记录一个事务
		for (Record rc : list) {
			final Integer RightID = rc.getInt("RightID"), Rank = rc.getInt("Rank");
			Db.tx(new IAtom() {
				@SuppressWarnings("finally")
				@Override
				public boolean run() throws SQLException {
					boolean isSuccess = true;
					try {
						/**
						 * 为每个权益规则对应的所有卡删除权益
						 */
						// 1封装批量删除的sql进行执行
						isSuccess = RightRuleDao.dao.deleteCardRight(RightID, Rank);
						// 2删除对应的rightcounttemp数据
						isSuccess = RightRuleDao.dao.deleteRightCountTemp(RightID, Rank);
						// 3更改rightcount对应数据的状态
						isSuccess = RightRuleDao.dao.editRightCountVID(RightID, Rank,
								VerifyStatus.stopUsingCompleteRightRule.code);
					} catch (Exception e) {
						isSuccess = false;
						BaseService.log.error("删除权益规则的卡权益时 rightId:" + RightID + " Rank:" + Rank + e);
					} finally {
						return isSuccess;
					}
				}
			});
		}
		BaseService.log.info("结束卡权益删除任务共处理" + list.size() + "个权益规则");
	}

}

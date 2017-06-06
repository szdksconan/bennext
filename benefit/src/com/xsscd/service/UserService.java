package com.xsscd.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.CardInfoDao;
import com.xsscd.dao.RoleDao;
import com.xsscd.dao.UserDao;
import com.xsscd.dao.VerifyStatusDao;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.AuditEntityException;
import com.xsscd.exception.DeleteEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.exception.OperateEntityException;
import com.xsscd.util.EmailUtil;
import com.xsscd.util.JfinalUtil;
import com.xsscd.util.MD5Util;
import com.xsscd.util.MessageUtil;
import com.xsscd.util.URLUtil;
import com.xsscd.vo.ResultVo;
import com.xsscd.vo.Roles;
import com.xsscd.vo.VerifyStatus;

public class UserService extends BaseService {

	public static UserService service = new UserService();

	/**
	 * 后台用户
	 */
	@Override
	public boolean add(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("UName", ct.getPara("UName"));
		rd.set("UPassword", MD5Util.getEncryptString("000000"));
		rd.set("RealName", ct.getPara("RealName"));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		rd.set("RID", ct.getParaToInt("RID"));
		// 未审核
		rd.set("VID", VerifyStatus.noAuditBackUser.code);
		Integer RID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		if (ct.getParaToInt("RID") <= RID) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "不能添加上级或同级角色的用户");
			throw ae;
		}
		// 重复信息
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", rd.get("UName"));
		if (JfinalUtil.findRecord("user", cv1) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的用户名存在重复");
			throw ae;
		}
		Map<String, Object> cv2 = new HashMap<String, Object>();
		cv2.put("Phone", rd.get("Phone"));
		if (JfinalUtil.findRecord("user", cv2) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的手机号存在重复");
			throw ae;
		}
		Map<String, Object> cv3 = new HashMap<String, Object>();
		cv3.put("Email", rd.get("Email"));
		if (JfinalUtil.findRecord("user", cv3) != null) {
			AddEntityException ae = new AddEntityException();
			ae.getMap().put("errorInfo", "添加的邮箱存在重复");
			throw ae;
		}
		return UserDao.dao.add(rd);
	}

	/**
	 * 后台用户
	 */
	@Override
	public boolean edit(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("UName", ct.getPara("UName"));
		rd.set("RealName", ct.getPara("RealName"));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		rd.set("RID", ct.getParaToInt("RID"));
		// 是否是修改
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", rd.get("UName"));
		Record oldUser = JfinalUtil.findRecord("user", cv1);
		Integer RID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		if (oldUser.getInt("RID") <= RID) {
			EditEntityException ae = new EditEntityException();
			ae.getMap().put("errorInfo", "不能修改上级或同级角色的用户");
			throw ae;
		}
		// 重复信息
		Map<String, Object> cv2 = new HashMap<String, Object>();
		cv2.put("Phone", rd.get("Phone"));
		Record oldPhone = JfinalUtil.findRecord("user", cv2);
		if (oldPhone != null && !oldPhone.get("UName").equals(oldUser.get("UName"))) {
			EditEntityException ae = new EditEntityException();
			ae.getMap().put("errorInfo", "修改的手机号存在重复");
			throw ae;
		}
		Map<String, Object> cv3 = new HashMap<String, Object>();
		cv3.put("Email", rd.get("Email"));
		Record oldEmail = JfinalUtil.findRecord("user", cv3);
		if (oldEmail != null && !oldEmail.get("UName").equals(oldUser.get("UName"))) {
			EditEntityException ae = new EditEntityException();
			ae.getMap().put("errorInfo", "修改的邮箱存在重复");
			throw ae;
		}
		return UserDao.dao.edit(rd);
	}

	/**
	 * 后台用户
	 */
	@Override
	public boolean delete(Object id, Controller ct) throws Exception {
		Integer RID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("UName", id);
		Record duser = JfinalUtil.findRecord("user", cv);
		if (duser.getInt("RID") <= RID) {
			DeleteEntityException ae = new DeleteEntityException();
			ae.getMap().put("errorInfo", "不能删除上级或同级角色的用户");
			throw ae;
		}
		return UserDao.dao.delete(id);
	}

	/**
	 * 后台用户 格式 id_state
	 */
	@Override
	public boolean audit(String idState, Controller ct) throws Exception {
		Integer RID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		String[] is = idState.split("_");
		String UName = is[0];
		Integer VID = Integer.valueOf(is[1]);
		Map<String, Object> cv = new HashMap<String, Object>();
		cv.put("UName", UName);
		Record duser = JfinalUtil.findRecord("user", cv);
		if (duser.getInt("RID") <= RID) {
			throw new AuditEntityException("不能审核上级或同级角色的用户");
		}
		// 检测当前操作用户状态是否可以完成当前操作
		if (duser != null) {
			Integer oldVID = duser.getInt("VID");
			// 请求未通过
			if (VID.equals(VerifyStatus.auditNoPassBackUser.code)) {
				if (!oldVID.equals(VerifyStatus.noAuditBackUser.code)) {
					throw new AuditEntityException("当前状态不是" + VerifyStatus.noAuditBackUser.description + ",不能审核未通过");
				}
			} else
			// 请求通过
			if (VID.equals(VerifyStatus.auditPassBackUser.code)) {
				if (!(oldVID.equals(VerifyStatus.noAuditBackUser.code)
						|| oldVID.equals(VerifyStatus.auditNoPassBackUser.code) || oldVID
						.equals(VerifyStatus.stopUsingBackUser.code))) {
					throw new AuditEntityException("当前状态不是" + VerifyStatus.noAuditBackUser.description + "或"
							+ VerifyStatus.auditNoPassBackUser.description + "或"
							+ VerifyStatus.stopUsingBackUser.description + ",不能审核通过");
				}

			} else
			// 请求停用
			if (VID.equals(VerifyStatus.stopUsingBackUser.code)) {
				if (!oldVID.equals(VerifyStatus.auditPassBackUser.code)) {
					throw new AuditEntityException("当前状态不是" + VerifyStatus.auditPassBackUser.description + ",不能审核停用");
				}

			} else {
				throw new AuditEntityException("不明白的审核操作!");
			}
		} else {
			throw new AuditEntityException("审核的用户数据不存在");
		}
		Record rd = new Record();
		rd.set("UName", UName);
		rd.set("VID", VID);
		return UserDao.dao.edit(rd);
	}

	/**
	 * 后台用户
	 */
	public void findBackUser(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("RID", ct.getParaToInt("RID"));
		rd.set("VID", ct.getParaToInt("VID"));
		rd.set("UName", URLUtil.decodeUTF8(ct.getPara("UName")));
		Integer CurRID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		rd.set("CurRID", CurRID);
		Page<Record> page = UserDao.dao.find(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询后台用户数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);
	}

	public void findBackUserRoleIdName(Controller ct) {
		Integer RID = ((Record) (ct.getSessionAttr("backUser"))).getInt("RID");
		String operate = ct.getPara("operate");
		if ("query".equals(operate)) {
			RID--;
		} else if ("update".equals(operate)) {

		} else {
			ct.renderJson(new ResultVo(false, "参数错误"));
			return;
		}
		List<Record> list = RoleDao.dao.findBackUserRoleIdName(RID);
		ResultVo rv = new ResultVo(true, "查询后台用户角色数据成功", list, list.size());
		ct.renderJson(rv);
	}

	public void findBackUserVerifyStatusIdName(Controller ct) {
		List<Record> list = VerifyStatusDao.dao.findBackUserVerifyStatusIdName();
		ResultVo rv = new ResultVo(true, "查询后台用户状态数据成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * ---------------------------------后台用户和 持卡人通用------------------------------------------------------------
	 */
	public void validateUName(Controller ct) {
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", URLUtil.decodeUTF8(ct.getPara("UName")));
		ResultVo rv = new ResultVo(true, "不存在该用户名");
		if (JfinalUtil.findRecord("user", cv1) != null) {
			rv.setIsSuccess(false);
			rv.setMessage("已存在该用户名");
		}
		ct.renderJson(rv);
	}

	public void validatePhone(Controller ct) {
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("Phone", URLUtil.decodeUTF8(ct.getPara("Phone")));
		ResultVo rv = new ResultVo(true, "不存在该手机号");
		if (JfinalUtil.findRecord("user", cv1) != null) {
			rv.setIsSuccess(false);
			rv.setMessage("已存在该手机号");
		}
		ct.renderJson(rv);
	}

	public void validateEmail(Controller ct) {
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("Email", URLUtil.decodeUTF8(ct.getPara("Email")));
		ResultVo rv = new ResultVo(true, "不存在该邮箱");
		if (JfinalUtil.findRecord("user", cv1) != null) {
			rv.setIsSuccess(false);
			rv.setMessage("已存在该邮箱");
		}
		ct.renderJson(rv);
	}

	public void loginBackUser(Controller ct) {
		String UName = URLUtil.decodeUTF8(ct.getPara("UName"));
		String UPassword = ct.getPara("UPassword");
		ResultVo rv = new ResultVo(true, "登录成功");
		try {
			UPassword = MD5Util.getEncryptString(UPassword);
			Map<String, Object> cv1 = new HashMap<String, Object>();
			cv1.put("UName", UName);
			Record user = JfinalUtil.findRecord("user", cv1);
			if (user == null) {
				rv.setIsSuccess(false);
				rv.setMessage("不存在该用户名");
			} else if (!user.get("UPassword").equals(UPassword)) {
				rv.setIsSuccess(false);
				rv.setMessage("密码不正确");
			} else if (user.get("RID").equals(Roles.cardUser.roleId)) {
				rv.setIsSuccess(false);
				rv.setMessage("持卡人不能登录后台系统");
			} else if (!user.get("VID").equals(VerifyStatus.auditPassBackUser.code)) {
				rv.setIsSuccess(false);
				rv.setMessage("用户的状态不允许登录");
			} else {
				// 登录成功放入当前用户信息到session
				ct.setSessionAttr("backUser", user);
				ct.removeSessionAttr("cardUser");
				// 设置当前用户的所有权限到session
				setUserPermissionsToSession(ct, user.getInt("RID"));
			}
		} catch (Exception e) {
			rv.setIsSuccess(false);
			rv.setMessage("系统繁忙");
			BaseService.log.error("登录后台用户出错" + e);
		}
		ct.renderJson(rv);
	}

	public void editPwdForOldPwd(Controller ct) throws Exception {
		Record backUser = ct.getSessionAttr("backUser");
		Record user = (Record) (backUser != null ? backUser : ct.getSessionAttr("cardUser"));
		String oldPwd = ct.getPara("oldUPassword");
		if (StringUtils.isBlank(oldPwd)) {
			OperateEntityException oe = new OperateEntityException();
			oe.getMap().put("errorInfo", "原密码没有输入");
			throw oe;
		}
		if (!user.get("UPassword").toString().equals(MD5Util.getEncryptString(oldPwd))) {
			OperateEntityException oe = new OperateEntityException();
			oe.getMap().put("errorInfo", "原密码不正确");
			throw oe;
		}
		String newPwd = ct.getPara("UPassword");
		Record edit = new Record();
		edit.set("UName", user.get("UName"));
		edit.set("UPassword", MD5Util.getEncryptString(newPwd));
		boolean isSucess = UserDao.dao.edit(edit);
		if (!isSucess) {
			throw new Exception();
		}
		// 更新session中密码
		// user.set("UPassword", newPwd);
		ct.removeSessionAttr("backUser");
		ct.removeSessionAttr("cardUser");
	}

	public void editPwdForVerifyCode(Controller ct) throws Exception {
		String Phone = ct.getPara("Phone");
		String Email = ct.getPara("Email");
		Integer vcode = ct.getParaToInt("vcode");
		String newPwd = MD5Util.getEncryptString(ct.getPara("UPassword"));
		ResultVo rv = new ResultVo(true, "修改成功");
		if (StringUtil.isNotBlank(Phone)) {
			Integer realVcode = ct.getSessionAttr("vcode");
			if (realVcode.equals(vcode)) {
				Map<String, Object> cv1 = new HashMap<String, Object>();
				cv1.put("Phone", ct.getPara("Phone"));
				Record edit = new Record();
				edit.set("UName", JfinalUtil.findRecord("user", cv1).get("UName"));
				edit.set("UPassword", newPwd);
				UserDao.dao.edit(edit);
				ct.removeSessionAttr("backUser");
				ct.removeSessionAttr("cardUser");
			} else {
				rv.setIsSuccess(false);
				rv.setMessage("验证码不正确");
			}
		} else if (StringUtil.isNotBlank(Email)) {
			Integer realVcode = ct.getSessionAttr("vcode");
			if (realVcode.equals(vcode)) {
				Map<String, Object> cv1 = new HashMap<String, Object>();
				cv1.put("Email", ct.getPara("Email"));
				Record edit = new Record();
				edit.set("UName", JfinalUtil.findRecord("user", cv1).get("UName"));
				edit.set("UPassword", newPwd);
				UserDao.dao.edit(edit);
				ct.removeSessionAttr("backUser");
				ct.removeSessionAttr("cardUser");
			} else {
				rv.setIsSuccess(false);
				rv.setMessage("验证码不正确");
			}
		} else {
			rv.setIsSuccess(false);
			rv.setMessage("请输入手机号或邮箱");
		}
		ct.renderJson(rv);
	}

	public void sendVerifyCode(Controller ct) throws Exception {
		String Phone = ct.getPara("Phone");
		String Email = ct.getPara("Email");
		ResultVo rv = new ResultVo();
		Integer vcode = (int) (Math.random() * 900000 + 100000);
		if (StringUtil.isNotBlank(Phone)) {
			Map<String, Object> cv1 = new HashMap<String, Object>();
			cv1.put("Phone", ct.getPara("Phone"));
			Record oldPhone = JfinalUtil.findRecord("user", cv1);
			if (oldPhone != null) {
				// TODO 检查-发送验证码到手机
				if (!MessageUtil.msg.send(Phone, "用于修改密码的验证码,验证码:" + vcode + " "))
					throw new OperateEntityException("发送短信验证码失败!请尝试邮箱找回密码");
				rv.setIsSuccess(true);
				rv.setMessage("验证码已发送到手机:" + Phone);
			} else {
				throw new OperateEntityException("该手机号没有用户使用");
			}
		} else if (StringUtil.isNotBlank(Email)) {
			Map<String, Object> cv1 = new HashMap<String, Object>();
			cv1.put("Email", ct.getPara("Email"));
			Record oldEmail = JfinalUtil.findRecord("user", cv1);
			if (oldEmail != null) {
				ct.setSessionAttr("vcode", vcode);
				EmailUtil eu = new EmailUtil();
				eu.sendEmail(Email, "用于修改密码的验证码", "验证码:" + vcode + " ");
				rv.setIsSuccess(true);
				rv.setMessage("验证码已发送到邮箱:" + Email);
			} else {
				rv.setIsSuccess(false);
				rv.setMessage("该邮箱没有用户使用");
			}
		} else {
			rv.setIsSuccess(false);
			rv.setMessage("请输入手机号或邮箱");
		}
		ct.renderJson(rv);
	}

	public void findBankAndCardName(Controller ct) {
		String CardNum = ct.getPara("CardNum");
		Record rd = CardInfoDao.dao.findBankAndCardName(CardNum);
		ResultVo rv = new ResultVo(false, "该卡号不被支持");
		if (rd != null) {
			rv.setIsSuccess(true);
			rv.setMessage("查询发卡银行和卡名称成功");
			List<Record> list = new ArrayList<Record>();
			list.add(rd);
			rv.setItemList(list);
		}
		ct.renderJson(rv);
	}

	/**
	 * ---------------------------------持卡人------------------------------------------------------------
	 */
	public void addCardUser(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("UName", ct.getPara("UName"));
		rd.set("UPassword", MD5Util.getEncryptString("000000"));
		rd.set("RealName", ct.getPara("RealName"));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		rd.set("RID", Roles.cardUser.roleId);
		// 重复信息
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", rd.get("UName"));
		if (JfinalUtil.findRecord("user", cv1) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的用户名存在重复");
			throw ae;
		}
		Map<String, Object> cv2 = new HashMap<String, Object>();
		cv2.put("Phone", rd.get("Phone"));
		if (JfinalUtil.findRecord("user", cv2) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的手机号存在重复");
			throw ae;
		}
		Map<String, Object> cv3 = new HashMap<String, Object>();
		cv3.put("Email", rd.get("Email"));
		if (JfinalUtil.findRecord("user", cv3) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的邮箱存在重复");
			throw ae;
		}
		UserDao.dao.add(rd);
	}

	public void editCardUser(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("UName", ct.getPara("UName"));
		rd.set("RealName", ct.getPara("RealName"));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		// 重复信息
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", rd.get("UName"));
		Record oldUser = JfinalUtil.findRecord("user", cv1);
		Map<String, Object> cv2 = new HashMap<String, Object>();
		cv2.put("Phone", rd.get("Phone"));
		Record oldPhone = JfinalUtil.findRecord("user", cv2);
		if (oldPhone != null && !oldPhone.get("UName").equals(oldUser.get("UName"))) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "修改的手机号存在重复");
			throw ae;
		}
		Map<String, Object> cv3 = new HashMap<String, Object>();
		cv3.put("Email", rd.get("Email"));
		Record oldEmail = JfinalUtil.findRecord("user", cv3);
		if (oldEmail != null && !oldEmail.get("UName").equals(oldUser.get("UName"))) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "修改的邮箱存在重复");
			throw ae;
		}
		UserDao.dao.edit(rd);
	}

	public void deleteCardUser(String id) throws Exception {
		UserDao.dao.delete(id);
	}

	public void findCardUser(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("UName", ct.getPara("UName"));
		Page<Record> page = UserDao.dao.findCardUser(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询持卡人数据成功", page.getList(), page.getTotalRow());
		ct.renderJson(rv);

	}

	/**
	 * 后台查询卡权益
	 */
	public void findCardRight(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("UName", URLUtil.decodeUTF8(ct.getPara("UName")));
		rd.set("BankName", URLUtil.decodeUTF8(ct.getPara("BankName")));
		rd.set("CardNum", URLUtil.decodeUTF8(ct.getPara("CardNum")));
		rd.set("CardTypeId", ct.getParaToInt("CardTypeId"));
		// rd.set("SupplyerName", URLUtil.decodeUTF8(ct.getPara("SupplyerName")));
		rd.set("RightName", URLUtil.decodeUTF8(ct.getPara("RightName")));
		Page<Record> page = UserDao.dao.findCardRight(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询卡权益数据成功", page.getList(), page.getTotalRow());
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));

	}

	/**
	 * 后台查询服务支持的商家列表
	 */
	public void findServiceSupplyerList(Controller ct) {
		List<Record> list = UserDao.dao.findServiceSupplyerList(ct.getParaToInt("serviceId"));
		JfinalUtil.convertNullToNullString(list);
		ResultVo rv = new ResultVo(true, "查询商家列表信息成功", list, list.size());
		ct.renderJson(rv);
	}

	/**
	 * -------------------------------------前台系统----------------------------------------------------
	 * 
	 * @param ct
	 * @throws NoSuchAlgorithmException
	 */
	public void loginCardUser(Controller ct) {
		String UName = ct.getPara("UName");
		String UPassword = ct.getPara("UPassword");
		ResultVo rv = new ResultVo(true, "登录成功");
		try {
			UPassword = MD5Util.getEncryptString(UPassword);
			Map<String, Object> cv1 = new HashMap<String, Object>();
			cv1.put("UName", UName);
			Record user = JfinalUtil.findRecord("user", cv1);
			if (user == null) {
				rv.setIsSuccess(false);
				rv.setMessage("不存在该用户名");
			} else if (!user.get("UPassword").equals(UPassword)) {
				rv.setIsSuccess(false);
				rv.setMessage("密码不正确");
			} else if (!user.get("RID").equals(Roles.cardUser.roleId)) {
				rv.setIsSuccess(false);
				rv.setMessage("后台用户不能登录前台系统");
			} else {
				// 登录成功放入当前用户信息到session
				ct.setSessionAttr("cardUser", user);
				ct.removeSessionAttr("backUser");
			}
		} catch (Exception e) {
			rv.setIsSuccess(false);
			rv.setMessage("系统繁忙");
			BaseService.log.error("登录持卡人出错" + e);
		}

		ct.renderJson(rv);
	}

	/**
	 * 绑卡-发送验证码
	 * 
	 * @param ct
	 * @throws Exception
	 */
	public void frontBindCard(Controller ct) throws Exception {
		//
		String CardNum = ct.getPara("CardNum");
		Record cardBin = CardInfoDao.dao.findCardBinInfo(CardNum);
		String bankCode = null;
		Integer CardTypeID = null;
		if (cardBin == null) {
			cardBin = CardInfoDao.dao.findOtherCardBinInfo(CardNum);
			if (cardBin == null)
				throw new OperateEntityException("该卡号不被支持");
			bankCode = cardBin.getStr("bankCode");
			CardTypeID = -1;
		} else {
			bankCode = cardBin.getStr("bankCode");
			CardTypeID = cardBin.getInt("cardRank");
		}
		String UName = ((Record) ct.getSessionAttr("cardUser")).getStr("UName");
		String Phone = ct.getPara("Phone");
		String HolderName = ct.getPara("HolderName");
		// TODO 这里发送请求到银联核对 手机号和卡号是否可以绑卡，通过之后进行下面操作
		// 封装信息
		Record cardinfo = new Record();
		cardinfo.set("UName", UName);
		cardinfo.set("HolderName", HolderName);
		cardinfo.set("BankID", bankCode);
		cardinfo.set("CardTypeID", CardTypeID);
		cardinfo.set("Phone", Phone);
		cardinfo.set("CardNum", CardNum);
		// 添加新的卡信息或修改原卡信息
		if (!CardInfoDao.dao.addOrEditCardInfo(cardinfo))
			throw new Exception("添加卡信息出错！");
	}

	/**
	 * 绑卡-判断验证码-完成绑卡
	 */
	public void frontBindCardCheckVcode(Controller ct) throws Exception {
		// TODO 这里核对验证码
	}

	/**
	 * 解绑
	 * 
	 * @param ct
	 * @throws Exception
	 */
	public void frontUnBindCard(Controller ct) throws Exception {
		String CardNum = ct.getPara("CardNum");
		String UPassword = ct.getPara("UPassword");
		Record user = (Record) ct.getSessionAttr("cardUser");
		String realUPassword = user.getStr("UPassword");
		if (realUPassword.equals(MD5Util.getEncryptString(UPassword))) {
			Record cardinfo = new Record();
			cardinfo.set("UName", "");
			cardinfo.set("CardNum", CardNum);
			// 拿到卡信息
			Record rd = CardInfoDao.dao.findCardInfo(CardNum);
			if (!rd.get("UName").equals(user.get("UName")))
				throw new OperateEntityException("解绑失败,该卡不是当前用户的!");
			// 删除卡信息的用户信息
			if (!CardInfoDao.dao.editCardInfo(cardinfo))
				throw new Exception("修改卡用户信息失败");
		} else {
			throw new OperateEntityException("解绑失败,密码不正确!");
		}
	}

	// 查询当前用户卡权益列表
	public void findCardUserCardRight(Controller ct) {
		Record rd = new Record();
		rd.set("pageNum", ct.getParaToInt("pageNum"));
		rd.set("limit", ct.getParaToInt("limit"));
		rd.set("UName", ((Record) ct.getSessionAttr("cardUser")).getStr("UName"));
		rd.set("RightName", ct.getPara("RightName"));
		Page<Record> page = UserDao.dao.findCardUserCardRight(rd);
		// Integer count = UserDao.dao.findCardUserCardRightCount(rd);
		JfinalUtil.convertNullToNullString(page.getList());
		ResultVo rv = new ResultVo(true, "查询持卡人权益数据成功", page.getList(), page.getTotalRow());
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));

	}

	public void findCardUserRightDetail(Controller ct) {
		Integer RightID = ct.getParaToInt("rightId");
		Integer supplyerId = ct.getParaToInt("supplyerId");
		String UName = ((Record) ct.getSessionAttr("cardUser")).getStr("UName");
		List<Record> cardRightCountList = UserDao.dao.findCardRightCount(UName, RightID);
		JfinalUtil.convertNullToNullString(cardRightCountList);
		Record rightDetail = UserDao.dao.findRightDetail(RightID, supplyerId);
		JfinalUtil.convertNullToNullString(rightDetail);
		rightDetail.set("cardRightCountList", cardRightCountList);
		List<Record> list = new ArrayList<Record>();
		list.add(rightDetail);
		ResultVo rv = new ResultVo(true, "查询持卡人权益详细数据成功", list, 1);
		String jsonString = JsonKit.toJson(rv);
		ct.renderJson(jsonString.replaceAll("\\\\", ""));
	}

	public void findCardUserDetail(Controller ct) {
		Record user = (Record) ct.getSessionAttr("cardUser");
		JfinalUtil.convertNullToNullString(user);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("UName", user.get("UName"));
		map.put("RealName", user.get("RealName"));
		map.put("Phone", user.get("Phone"));
		map.put("Email", user.get("Email"));
		String UName = user.getStr("UName");
		List<Record> cardList = UserDao.dao.findCardUserCardInfo(UName);
		JfinalUtil.convertNullToNullString(cardList);
		map.put("cardList", cardList);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
		ResultVo rv = new ResultVo(true, "查询持卡人基本信息数据成功", list, 1);
		ct.renderJson(rv);
	}

	public void registerCardUser(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("UName", ct.getPara("UName"));
		rd.set("UPassword", MD5Util.getEncryptString(ct.getPara("UPassword")));
		rd.set("RealName", ct.getPara("RealName"));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		rd.set("RID", Roles.cardUser.roleId);
		// 重复信息
		Map<String, Object> cv1 = new HashMap<String, Object>();
		cv1.put("UName", rd.get("UName"));
		if (JfinalUtil.findRecord("user", cv1) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的用户名存在重复");
			throw ae;
		}
		Map<String, Object> cv2 = new HashMap<String, Object>();
		cv2.put("Phone", rd.get("Phone"));
		if (JfinalUtil.findRecord("user", cv2) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的手机号存在重复");
			throw ae;
		}
		Map<String, Object> cv3 = new HashMap<String, Object>();
		cv3.put("Email", rd.get("Email"));
		if (JfinalUtil.findRecord("user", cv3) != null) {
			OperateEntityException ae = new OperateEntityException();
			ae.getMap().put("errorInfo", "添加的邮箱存在重复");
			throw ae;
		}
		UserDao.dao.add(rd);
	}

	private void setUserPermissionsToSession(Controller ct, Integer roleId) {
		//
		List<Record> permissions = UserDao.dao.findRolePermissions(roleId);
		List<String> permissionUris = new ArrayList<String>();
		for (Record rd : permissions) {
			permissionUris.add(rd.getStr("Uri"));
		}
		ct.setSessionAttr("permissionUris", permissionUris);
	}

	public void loginOut(Controller ct) {
		ResultVo rv = new ResultVo(true, "注销成功");
		ct.removeSessionAttr("cardUser");
		ct.removeSessionAttr("backUser");
		ct.renderJson(rv);
	}

	public void findMenus(Controller ct) {
		ResultVo rv = new ResultVo(true, "查询当前用户菜单列表成功");
		Integer RID = ((Record) ct.getSessionAttr("backUser")).getInt("RID");
		List<Record> menus = UserDao.dao.findMenus(RID);
		// 找到一级菜单
		/**
		 * { menuName:[menuName,menuName,menuName],menuName:[menuName,menuName,menuName]}
		 */
		Record oneMenus = new Record();
		for (Record m : menus) {
			Integer grade = m.getInt("grade");
			Integer pGrade = m.getInt("pGrade");
			String menuName = m.getStr("menuName");
			String pMenuName = m.getStr("pMenuName");
			if (grade == 1) {
				oneMenus.set(menuName, new ArrayList<String>());
			} else if (pGrade == 1) {
				oneMenus.set(pMenuName, new ArrayList<String>());
			}
		}
		// 给一级菜单设置子菜单
		for (Record m : menus) {
			Integer grade = m.getInt("grade");
			String menuName = m.getStr("menuName");
			String pMenuName = m.getStr("pMenuName");
			if (grade == 2) {
				List<String> twoMenus = oneMenus.get(pMenuName);
				twoMenus.add(menuName);
			}
		}
		if (oneMenus.getColumns().size() == 0) {
			rv.setIsSuccess(false);
			rv.setMessage("该用户下没有对应的菜单");
		} else {
			List<Record> list = new ArrayList<Record>();
			list.add(oneMenus);
			rv.setItemList(list);
		}
		ct.renderJson(rv);

	}

	public void findPermissionsForMenuName(Controller ct) {
		ResultVo rv = new ResultVo(true, "查询当前菜单权限列表成功");
		Integer RID = ((Record) ct.getSessionAttr("backUser")).getInt("RID");
		String menuName = URLUtil.decodeUTF8(ct.getPara("menuName"));
		List<Record> permissions = UserDao.dao.findPermissionsForMenuName(RID, menuName);
		if (permissions.size() == 0) {
			rv.setIsSuccess(false);
			rv.setMessage("该菜单下没有对应的权限");
		} else {
			rv.setItemList(permissions);
		}
		ct.renderJson(rv);
	}

	// 后台修改用户信息
	public void backEditLoginUserInfo(Controller ct) throws Exception {
		Record rd = new Record();
		rd.set("RealName", URLUtil.decodeUTF8(ct.getPara("RealName")));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		String pwd = ct.getPara("UPassword");
		Record backUser = ct.getSessionAttr("backUser");
		if (backUser.get("UPassword").equals(MD5Util.getEncryptString(pwd))) {
			// 重复信息
			Map<String, Object> cv2 = new HashMap<String, Object>();
			cv2.put("Phone", rd.get("Phone"));
			Record oldPhone = JfinalUtil.findRecord("user", cv2);
			if (oldPhone != null && !oldPhone.get("UName").equals(backUser.get("UName"))) {
				throw new OperateEntityException("修改的手机号存在重复");
			}
			Map<String, Object> cv3 = new HashMap<String, Object>();
			cv3.put("Email", rd.get("Email"));
			Record oldEmail = JfinalUtil.findRecord("user", cv3);
			if (oldEmail != null && !oldEmail.get("UName").equals(backUser.get("UName"))) {
				throw new OperateEntityException("修改的邮箱存在重复");
			}
			rd.set("UName", backUser.get("UName"));
			if (!UserDao.dao.edit(rd))
				throw new OperateEntityException("修改当前用户信息失败");
			Record bu = ct.getSessionAttr("backUser");
			bu.set("RealName", rd.get("RealName"));
			bu.set("Phone", rd.get("Phone"));
			bu.set("Email", rd.get("Email"));
		} else {
			throw new OperateEntityException("密码不正确");
		}

	}

	// 前台修改用户信息
	public void frontEditLoginUserInfo(Controller ct) throws Exception {

		Record rd = new Record();
		rd.set("RealName", URLUtil.decodeUTF8(ct.getPara("RealName")));
		rd.set("Phone", ct.getPara("Phone"));
		rd.set("Email", ct.getPara("Email"));
		String pwd = ct.getPara("UPassword");
		Record cardUser = ct.getSessionAttr("cardUser");
		if (cardUser.get("UPassword").equals(MD5Util.getEncryptString(pwd))) {
			// 重复信息
			Map<String, Object> cv2 = new HashMap<String, Object>();
			cv2.put("Phone", rd.get("Phone"));
			Record oldPhone = JfinalUtil.findRecord("user", cv2);
			if (oldPhone != null && !oldPhone.get("UName").equals(cardUser.get("UName"))) {
				throw new OperateEntityException("修改的手机号存在重复");
			}
			Map<String, Object> cv3 = new HashMap<String, Object>();
			cv3.put("Email", rd.get("Email"));
			Record oldEmail = JfinalUtil.findRecord("user", cv3);
			if (oldEmail != null && !oldEmail.get("UName").equals(cardUser.get("UName"))) {
				throw new OperateEntityException("修改的邮箱存在重复");
			}
			rd.set("UName", cardUser.get("UName"));
			if (!UserDao.dao.edit(rd))
				throw new OperateEntityException("修改当前用户信息失败");
			Record cu = ct.getSessionAttr("cardUser");
			cu.set("RealName", rd.get("RealName"));
			cu.set("Phone", rd.get("Phone"));
			cu.set("Email", rd.get("Email"));
		} else {
			throw new OperateEntityException("密码不正确");
		}

	}
}

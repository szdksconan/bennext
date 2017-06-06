package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.entity.User;
import com.xsscd.vo.Roles;

public class UserDao {
	public static UserDao dao = new UserDao();

	public User login(String uName, String psw) {
		User user = null;
		String sql = "select * from user where UName=? and UPassword=?";
		Record r = Db.findFirst(sql, uName, psw);
		if (r != null) {
			user = new User();
			user.setuName(r.getStr("UName"));
			user.setuPassword(r.getStr("UPassword"));
			user.setRealName(r.getStr("RealName"));
			user.setPhone(r.getStr("Phone"));
			user.setEmail(r.getStr("Email"));
			user.setRid(r.getInt("RID"));
			user.setVid(r.getInt("VID"));
		}
		return user;
	}

	public User findUserBySingleCondition(String key, String value) {
		User user = null;
		String sql = "select * from user where " + key + " = ?";
		Record r = Db.findFirst(sql, value);
		if (r != null) {
			user = new User();
			user.setuName(r.getStr("UName"));
			user.setuPassword(r.getStr("UPassword"));
			user.setRealName(r.getStr("RealName"));
			user.setPhone(r.getStr("Phone"));
			user.setEmail(r.getStr("Email"));
			user.setRid(r.getInt("RID"));
			user.setVid(r.getInt("VID"));
		}
		return user;
	}

	public void insert(User regUser) {
		Record r = new Record();
		r.set("Uname", regUser.getuName());
		r.set("UPassword", regUser.getuPassword());
		r.set("RealName", regUser.getRealName());
		r.set("Phone", regUser.getPhone());
		r.set("Email", regUser.getEmail());
		r.set("RID", regUser.getRid());
		r.set("VID", regUser.getVid());
		Db.save("user", r);
	}

	/**
	 * --------------------------------------------zengcy 添加-------------------------------------------------------
	 */
	public boolean add(Record record) throws Exception {
		return Db.save("user", "UName", record);
	}

	/**
	 * 主键修改
	 */
	public boolean edit(Record record) throws Exception {
		return Db.update("user", "UName", record);
	}

	public boolean delete(Object id) throws Exception {
		return Db.deleteById("user", "UName", id);
	}

	// 用户名 角色 状态
	public Page<Record> find(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" select u.UName,u.RealName,r.RID,r.RName, ");
		sql1.append(" u.Phone,u.Email,vs.VID,vs.VDescription");
		StringBuilder sql2 = new StringBuilder(" from user u");
		sql2.append(" LEFT JOIN role r on u.RID=r.RID");
		sql2.append(" LEFT JOIN verifystatus vs on u.VID=vs.VID");
		sql2.append(" where 1=1 ");
		// 当前角色能查看的角色
		sql2.append(" and u.RID>=?");
		sql2.append(" and u.RID!=" + Roles.cardUser.roleId);
		param.add(params.get("CurRID"));
		String UName = params.get("UName");
		if (StringUtil.isNotBlank(UName)) {
			sql2.append(" and u.UName like ? ");
			param.add("%" + UName + "%");
		}
		Integer RID = params.get("RID");
		if (RID != null) {
			sql2.append(" and u.RID=?");
			param.add(RID);
		}
		Integer VID = params.get("VID");
		if (VID != null) {
			sql2.append(" and u.VID=?");
			param.add(VID);
		}
		sql2.append(" ORDER BY u.RID,u.VID");
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	// 用户名 角色 状态
	public Page<Record> findCardUser(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" select u.UName,u.RealName, u.Phone,u.Email ");
		StringBuilder sql2 = new StringBuilder(" from user u");
		sql2.append(" where 1=1 ");
		sql2.append(" and u.RID=" + Roles.cardUser.roleId);
		String UName = params.get("UName");
		if (StringUtil.isNotBlank(UName)) {
			sql2.append(" and u.UName like ? ");
			param.add("%" + UName + "%");
		}
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	public Page<Record> findCardRight(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder(
				" select ci.UName,bi.BankName,CONCAT(ct.Description,'(',ct.CardType,')') CardName,");
		sql1.append(" ci.CardNum,cr.Count,ri.Display RightName,s.startDate,s.endDate,ri.NeedMoney/100 NeedMoney,s.serviceId ");
		// ,si.SupplyerName,si.Address,si.Contact,si.Longitude,si.Latitude
		StringBuilder sql2 = new StringBuilder(" from cardright cr");
		sql2.append(" LEFT JOIN cardinfo ci on cr.CardNum=ci.CardNum");
		sql2.append(" LEFT JOIN cardtype ct on ci.CardTypeID=ct.CardTypeID");
		sql2.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql2.append(" LEFT JOIN rightinfo ri on cr.RightID=ri.RightID");
		sql2.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
		// sql2.append(" LEFT JOIN service_supplyerinfo ss on s.serviceId=ss.serviceId");
		// sql2.append(" LEFT JOIN supplyerinfo si on ss.supplyerId=si.supplyerId");
		sql2.append(" where 1=1 ");
		sql2.append(" and cr.Count>0");
		// 条件查询
		String UName = params.get("UName");
		if (StringUtil.isNotBlank(UName)) {
			sql2.append(" and ci.UName like ? ");
			param.add("%" + UName + "%");
		}
		String BankName = params.get("BankName");
		if (StringUtil.isNotBlank(BankName)) {
			sql2.append(" and bi.BankName like ? ");
			param.add("%" + BankName + "%");
		}
		Integer CardTypeId = params.get("CardTypeId");
		if (CardTypeId != null) {
			sql2.append(" and ct.CardTypeID = ? ");
			param.add(CardTypeId);
		}
		String CardNum = params.get("CardNum");
		if (StringUtil.isNotBlank(CardNum)) {
			sql2.append(" and ci.CardNum like ? ");
			param.add("%" + CardNum + "%");
		}
		String RightName = params.get("RightName");
		if (StringUtil.isNotBlank(RightName)) {
			sql2.append(" and ri.Display like ? ");
			param.add("%" + RightName + "%");
		}
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	public Page<Record> findCardUserCardRight(Record params) {
		List<Object> param = new ArrayList<Object>();
		// 设置sql
		StringBuilder sql1 = new StringBuilder("select * ");
		StringBuilder sql2 = new StringBuilder(
				"from( select ri.Display,ri.RightID,si.supplyerName,si.address,si.supplyerId,fi.filePath from cardright cr");
		sql2.append(" LEFT JOIN cardinfo ci on cr.CardNum=ci.CardNum");
		sql2.append(" LEFT JOIN rightinfo ri on cr.RightID=ri.RightID");
		sql2.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
		sql2.append(" LEFT JOIN service_supplyerinfo ss on s.serviceId=ss.serviceId");
		sql2.append(" LEFT JOIN supplyerinfo si on ss.supplyerId=si.supplyerId");
		sql2.append(" LEFT JOIN fileinfo fi on fi.targetId=si.supplyerId and fi.targetTable='supplyerinfo'");

		sql2.append(" where 1=1 ");
		// 条件查询
		String UName = params.get("UName");
		sql2.append(" and ci.UName = ? ");
		param.add(UName);
		sql2.append(" and cr.Count>0");
		String RightName = params.get("RightName");
		if (StringUtil.isNotBlank(RightName)) {
			sql2.append(" and ri.Display like ? ");
			param.add("%" + RightName + "%");
		}
		sql2.append(" GROUP BY ri.RightID,ss.supplyerId ");
		sql2.append(" )t");
		return Db.paginate(params.getInt("pageNum"), params.getInt("limit"), sql1.toString(), sql2.toString(),
				param.toArray());
	}

	// public Integer findCardUserCardRightCount(Record params) {
	// List<Object> param = new ArrayList<Object>();
	// // 设置sql
	// StringBuilder sql2 = new StringBuilder("select count(*) count from (select ri.RightID from cardright cr");
	// sql2.append(" LEFT JOIN cardinfo ci on cr.CardNum=ci.CardNum");
	// sql2.append(" LEFT JOIN rightinfo ri on cr.RightID=ri.RightID");
	// sql2.append(" LEFT JOIN service s on ri.ServiceID=s.serviceId");
	// sql2.append(" LEFT JOIN service_supplyerinfo ss on s.serviceId=ss.serviceId");
	// sql2.append(" where 1=1 ");
	// // 条件查询
	// String UName = params.get("UName");
	// sql2.append(" and ci.UName = ? ");
	// param.add(UName);
	// sql2.append(" and cr.Count>0");
	// String RightName = params.get("RightName");
	// if (StringUtil.isNotBlank(RightName)) {
	// sql2.append(" and ri.Display like ? ");
	// param.add("%" + RightName + "%");
	// }
	// sql2.append(" GROUP BY ri.RightID,ss.supplyerId  )t");
	// Record rd = Db.findFirst(sql2.toString(), param.toArray());
	// return rd.getLong("count").intValue();
	// }

	public List<Record> findCardRightCount(String uName, Integer rightID) {
		StringBuilder sql = new StringBuilder(
				" select bi.BankName,CONCAT(ct.Description,'(',ct.CardType,')') CardName,ci.CardNum,cr.Count from cardright cr");
		sql.append(" LEFT JOIN cardinfo ci on cr.CardNum=ci.CardNum");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN cardtype ct on ci.CardTypeID=ct.CardTypeID");
		sql.append(" LEFT JOIN rightinfo ri on cr.RightID=ri.RightID");
		sql.append(" where cr.Count>0");
		sql.append(" and ci.UName=?");
		sql.append(" and ri.RightID=?");
		return Db.find(sql.toString(), uName, rightID);
	}

	public Record findRightDetail(Integer rightID, Integer supplyerId) {
		StringBuilder sql = new StringBuilder(
				" select si.address,si.contact,si.latitude,si.longitude,si.supplyerName,si.trafficDate,");
		sql.append(" ri.Display rightName,ri.DailyNumPerStore,ri.DailyNumTop,ri.NeedMoney/100 NeedMoney,");
		sql.append(" CASE ri.SelfRule1 when 1 then '首次收费'");
		sql.append(" when 2 then '每次收费'");
		sql.append(" when 3 then '不收费' END SelfRule1,");
		sql.append(" s.startDate,s.endDate,s.serviceProvision,fi.filePath");
		sql.append(" from rightinfo ri ");
		sql.append(" LEFT JOIN service s on ri.ServiceID =s.serviceId");
		sql.append(" LEFT JOIN supplyerinfo si on 1=1");
		sql.append(" LEFT JOIN fileinfo fi on fi.targetId=si.supplyerId and fi.targetTable='supplyerinfo'");

		sql.append(" where ri.RightID = ? and si.supplyerId =?");
		return Db.findFirst(sql.toString(), rightID, supplyerId);
	}

	//
	public List<Record> findCardUserCardInfo(String uName) {
		StringBuilder sql = new StringBuilder(
				" select bi.BankID,bi.BankName,CONCAT(ct.Description,'(',ct.CardType,')') CardName,ci.CardNum from cardinfo ci");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN cardtype ct on ci.CardTypeID=ct.CardTypeID");
		sql.append(" where ci.UName=?");
		return Db.find(sql.toString(), uName);
	}

	/**
	 * 根据角色id 查询所有的权限信息
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Record> findRolePermissions(Integer roleId) {
		StringBuilder sql = new StringBuilder(" select p.Uri from role_permission rp");
		sql.append(" LEFT JOIN permission p on rp.PID=p.PID");
		sql.append(" WHERE rp.RID=?");
		return Db.find(sql.toString(), roleId);
	}

	/**
	 * 获取二级菜单列表
	 */
	public List<Record> findMenus(Integer roleId) {
		StringBuffer sql = new StringBuffer(" SELECT m.menuName,m.grade,m.menuType,");
		sql.append(" mp.menuName pMenuName,mp.grade pGrade, mp.menuType pMenuType");
		sql.append(" from role_permission rp");
		sql.append(" LEFT JOIN permission p on rp.PID=p.PID");
		sql.append(" LEFT JOIN menu m on p.menuId=m.menuId ");
		sql.append(" LEFT JOIN menu mp on mp.menuId=m.parentMenuId ");
		sql.append(" where rp.RID=?");
		sql.append(" GROUP BY m.menuName");
		return Db.find(sql.toString(), roleId);
	}

	public List<Record> findPermissionsForMenuName(Integer roleId, String menuName) {
		StringBuffer sql = new StringBuffer(" SELECT p.PName");
		sql.append(" from role_permission rp");
		sql.append(" LEFT JOIN permission p on rp.PID=p.PID");
		sql.append(" LEFT JOIN menu m on p.menuId=m.menuId ");
		sql.append(" where rp.RID=?");
		sql.append(" and m.menuName=?");
		return Db.find(sql.toString(), roleId, menuName);
	}

	public List<Record> findServiceSupplyerList(Integer serviceId) {
		StringBuilder sql = new StringBuilder(
				"SELECT concat(si.supplyerName,'  商户尾号:', right(si.merchantCode,4),'  终端尾号:',right(si.termCode,4)) supplyerName ,si.address,si.contact,si.trafficDate,si.latitude,si.longitude");
		sql.append(" FROM service s");
		sql.append(" LEFT JOIN service_supplyerinfo ss ON s.serviceId = ss.serviceId");
		sql.append(" LEFT JOIN supplyerinfo si ON ss.supplyerId = si.supplyerId");
		sql.append(" WHERE s.serviceId=?");
		return Db.find(sql.toString(), serviceId);
	}
}

package com.xsscd.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.vo.Roles;

/**
 * 角色相关数据库访问
 * 
 * @author zengcy
 * 
 */
public class RoleDao {
	public static RoleDao dao = new RoleDao();

	public List<Record> findBackUserRoleIdName(Integer rID) {
		String sql = "select r.RID,r.RName from role r where r.RID > ? and r.RID!=5 and r.RID!=6";
		return Db.find(sql, rID);
	}

	//
	// public Page<Record> findPermission(Record rd) {
	// List<Object> param=new ArrayList<Object>();
	// Integer pageNum=rd.get("pageNum"),limit=rd.get("limit");
	// //设置sql
	// StringBuilder sql1=new StringBuilder(" select p.PID,p.PName ");
	// StringBuilder sql2=new StringBuilder(" from permission p");
	// sql2.append(" where 1=1 ");
	// String PName=rd.get("PName");
	// if(StringUtil.isNotBlank(PName)){
	// sql2.append(" and p.PName like ?");
	// param.add("%"+PName+"%");
	// }
	// return Db.paginate(pageNum, limit,sql1.toString() ,sql2.toString() , param.toArray());
	// }
	// public List<Record> findRole() {
	// String sql="SELECT RID,RName FROM role where RID<=4 ";
	// return Db.find(sql);
	// }

	public boolean addRolePermission(Integer rid, Integer pid) {
		String sql = "INSERT INTO role_permission(RID,PID) VALUES(?,?)";
		return 1 == Db.update(sql, rid, pid);
	}

	public boolean deleteRolePermission(Integer rid, Integer pid) {
		String sql = "delete from role_permission where RID=? and PID=?";
		return 1 == Db.update(sql, rid, pid);
	}

	public Page<Record> findRolePermissionState(Record rd) {
		Integer pageNum = rd.get("pageNum"), limit = rd.get("limit");
		// 设置sql
		StringBuilder sql1 = new StringBuilder(" select p.PID,CONCAT(p.PName,'->菜单:',m.menuName) PName,r.RID,r.RName,");
		sql1.append(" case WHEN rp.RID is not null then 'y' else  'n' end state");
		StringBuilder sql2 = new StringBuilder(" from permission p");
		sql2.append(" LEFT JOIN role r  on  r.RID!=" + Roles.systemMgr.roleId + " and r.RID!=" + Roles.cardUser.roleId);
		sql2.append(" LEFT JOIN role_permission rp on p.PID=rp.PID and r.RID=rp.RID ");
		sql2.append(" LEFT JOIN menu  m on p.menuId=m.menuId");
		sql2.append(" where p.PType!='sys' and p.PType!='front' and p.Uri not like '%_ro' and p.Uri not like '%_no' ORDER BY p.PID,r.RID ");

		return Db.paginate(pageNum, limit, sql1.toString(), sql2.toString());
	}

	public Record findPermisionByUri(String uri) {
		String sql = "select * from permission p where p.Uri=?";
		return Db.findFirst(sql, uri);
	}
}
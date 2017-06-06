package com.xsscd.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class DataStatisticsDao {
	public static DataStatisticsDao dao = new DataStatisticsDao();

	public Page<Record> findPOSRightRecord(Record rd) {
		List<Object> params = new ArrayList<Object>();
		String sqlSel = "SELECT *";
		StringBuilder sql = new StringBuilder(" from ( SELECT bi.BankName,ur.CardNum,ri.Display,");
		sql.append(" CONCAT(LEFT(CAST(ur.RecordTime AS char),4),ur.LocalTransDate,ur.LocalTransTime) PayDate,");
		sql.append(" ur.TermCode,ur.MerchantCode,ur.SupplyerName ");
		sql.append(" from usingrecord ur");
		sql.append(" LEFT JOIN cardinfo ci on ur.CardNum=ci.CardNum");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN rightinfo ri on ur.RightID=ri.RightID");
		sql.append(" where 1=1");
		sql.append(" and ur.state=1 ");
		if (StringUtils.isNotBlank(rd.getStr("BankName"))) {
			sql.append(" and bi.BankName LIKE ?");
			params.add("%" + rd.getStr("BankName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("CardNum"))) {
			sql.append(" and ur.CardNum LIKE ?");
			params.add("%" + rd.getStr("CardNum") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("RightName"))) {
			sql.append(" and ri.Display LIKE ?");
			params.add("%" + rd.getStr("RightName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("TermCode"))) {
			sql.append(" and ur.TermCode LIKE ?");
			params.add("%" + rd.getStr("TermCode") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("SupplyerName"))) {
			sql.append(" and ur.SupplyerName LIKE ?");
			params.add("%" + rd.getStr("SupplyerName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("MerchantCode"))) {
			sql.append(" and ur.MerchantCode LIKE ?");
			params.add("%" + rd.getStr("MerchantCode") + "%");
		}
		sql.append(" order by ur.RecordTime desc ");
		// 时间段控制
		sql.append(" )t where 1=1 ");
		if (StringUtils.isNotBlank(rd.getStr("startDate"))) {
			sql.append("AND t.PayDate>=?");
			params.add(rd.getStr("startDate"));
		}
		if (StringUtils.isNotBlank(rd.getStr("endDate"))) {
			sql.append("AND t.PayDate<=?");
			params.add(rd.getStr("endDate"));
		}
		return Db.paginate(rd.getInt("pageNum"), rd.getInt("limit"), sqlSel, sql.toString(), params.toArray());
	}

	public Page<Record> findPOSRightMonthRecord(Record rd) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sqlsel = new StringBuilder("SELECT * ");
		StringBuilder sql = new StringBuilder(" from ( ");
		sql.append("SELECT bi.BankName,LEFT(CONCAT(LEFT(CAST(ur.RecordTime AS char),4),ur.LocalTransDate),6) YearMonth,");
		sql.append("ri.Display,ur.SupplyerName,ur.MerchantCode,ur.TermCode,COUNT(*) rdCount from usingrecord ur ");
		sql.append(" LEFT JOIN cardinfo ci on ur.CardNum=ci.CardNum");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN rightinfo ri on ur.RightID=ri.RightID");
		sql.append(" where 1=1");
		sql.append(" and ur.state=1 ");
		if (StringUtils.isNotBlank(rd.getStr("BankName"))) {
			sql.append(" and bi.BankName LIKE ?");
			params.add("%" + rd.getStr("BankName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("RightName"))) {
			sql.append(" and ri.Display LIKE ?");
			params.add("%" + rd.getStr("RightName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("TermCode"))) {
			sql.append(" and ur.TermCode LIKE ?");
			params.add("%" + rd.getStr("TermCode") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("SupplyerName"))) {
			sql.append(" and ur.SupplyerName LIKE ?");
			params.add("%" + rd.getStr("SupplyerName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("MerchantCode"))) {
			sql.append(" and ur.MerchantCode LIKE ?");
			params.add("%" + rd.getStr("MerchantCode") + "%");
		}
		sql.append(" GROUP BY yearMonth,bi.BankID,MerchantCode,ur.TermCode,ur.RightID )t ");
		// 时间段控制
		sql.append("where 1=1 ");
		if (StringUtils.isNotBlank(rd.getStr("startMonth"))) {
			sql.append("AND t.YearMonth>=?");
			params.add(rd.getStr("startMonth"));
		}
		if (StringUtils.isNotBlank(rd.getStr("endMonth"))) {
			sql.append("AND t.YearMonth<=?");
			params.add(rd.getStr("endMonth"));
		}
		return Db.paginate(rd.getInt("pageNum"), rd.getInt("limit"), sqlsel.toString(), sql.toString(),
				params.toArray());
	}

	public List<Record> findAllPOSRightRecord(Record rd) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT * from ( SELECT bi.BankName,ur.CardNum,ri.Display,");
		sql.append(" CONCAT(LEFT(CAST(ur.RecordTime AS char),4),ur.LocalTransDate,ur.LocalTransTime) PayDate,");
		sql.append(" ur.TermCode,ur.MerchantCode,ur.SupplyerName ");
		sql.append(" from usingrecord ur");
		sql.append(" LEFT JOIN cardinfo ci on ur.CardNum=ci.CardNum");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN rightinfo ri on ur.RightID=ri.RightID");
		sql.append(" where 1=1");
		if (StringUtils.isNotBlank(rd.getStr("BankName"))) {
			sql.append(" and bi.BankName LIKE ?");
			params.add("%" + rd.getStr("BankName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("CardNum"))) {
			sql.append(" and ur.CardNum LIKE ?");
			params.add("%" + rd.getStr("CardNum") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("RightName"))) {
			sql.append(" and ri.Display LIKE ?");
			params.add("%" + rd.getStr("RightName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("TermCode"))) {
			sql.append(" and ur.TermCode LIKE ?");
			params.add("%" + rd.getStr("TermCode") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("SupplyerName"))) {
			sql.append(" and ur.SupplyerName LIKE ?");
			params.add("%" + rd.getStr("SupplyerName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("MerchantCode"))) {
			sql.append(" and ur.MerchantCode LIKE ?");
			params.add("%" + rd.getStr("MerchantCode") + "%");
		}
		sql.append(" order by ur.RecordTime desc ");
		// 时间段控制
		sql.append(" )t where 1=1 ");
		if (StringUtils.isNotBlank(rd.getStr("startDate"))) {
			sql.append("AND t.PayDate>=?");
			params.add(rd.getStr("startDate"));
		}
		if (StringUtils.isNotBlank(rd.getStr("endDate"))) {
			sql.append("AND t.PayDate<=?");
			params.add(rd.getStr("endDate"));
		}
		return Db.find(sql.toString(), params.toArray());
	}

	public List<Record> findAllPOSRightMonthRecord(Record rd) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT *  from ( ");
		sql.append("SELECT bi.BankName,LEFT(CONCAT(LEFT(CAST(ur.RecordTime AS char),4),ur.LocalTransDate),6) YearMonth,");
		sql.append("ri.Display,ur.SupplyerName,ur.MerchantCode,ur.TermCode,COUNT(*) rdCount from usingrecord ur ");
		sql.append(" LEFT JOIN cardinfo ci on ur.CardNum=ci.CardNum");
		sql.append(" LEFT JOIN bankinfo bi on ci.BankID=bi.BankID");
		sql.append(" LEFT JOIN rightinfo ri on ur.RightID=ri.RightID");
		sql.append(" where 1=1");
		if (StringUtils.isNotBlank(rd.getStr("BankName"))) {
			sql.append(" and bi.BankName LIKE ?");
			params.add("%" + rd.getStr("BankName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("RightName"))) {
			sql.append(" and ri.Display LIKE ?");
			params.add("%" + rd.getStr("RightName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("TermCode"))) {
			sql.append(" and ur.TermCode LIKE ?");
			params.add("%" + rd.getStr("TermCode") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("SupplyerName"))) {
			sql.append(" and ur.SupplyerName LIKE ?");
			params.add("%" + rd.getStr("SupplyerName") + "%");
		}
		if (StringUtils.isNotBlank(rd.getStr("MerchantCode"))) {
			sql.append(" and ur.MerchantCode LIKE ?");
			params.add("%" + rd.getStr("MerchantCode") + "%");
		}
		sql.append(" GROUP BY yearMonth,bi.BankID,MerchantCode,ur.TermCode,ur.RightID )t ");
		// 时间段控制
		sql.append("where 1=1 ");
		if (StringUtils.isNotBlank(rd.getStr("startMonth"))) {
			sql.append("AND t.YearMonth>=?");
			params.add(rd.getStr("startMonth"));
		}
		if (StringUtils.isNotBlank(rd.getStr("endMonth"))) {
			sql.append("AND t.YearMonth<=?");
			params.add(rd.getStr("endMonth"));
		}
		return Db.find(sql.toString(), params.toArray());
	}

}

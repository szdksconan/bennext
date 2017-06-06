package com.unipay.benext.utils;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar; 
import java.util.GregorianCalendar;

public class DateFormatUtil {
	/**
	 * @param  日期格式串
	 * @return 根据格式日期串格式后的当前日期
	 * @author frank.zhu
	 */
	public static String dateFormat(String format){
		String sDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sDate = sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sDate;
	}
	
	/**
	 * @param  需格式话的日期字符串
	 * @return 返回“yyyy-MM”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateParseToym(String sDate){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
		    date = sdf.parse(sDate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param  需格式话的日期字符串
	 * @return 返回“yyyy-MM-dd”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateParseToymd(String sDate){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		    date = sdf.parse(sDate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param 需要格式的日期字符串
	 * @return 返回“yyyy-MM-dd HH:mm:ss”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateParseToymdhms(String sDate){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		     date = sdf.parse(sDate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param  需要格式成日期的字符串
	 * @return 返回“HH:mm:ss”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateParseTohms(String sDate){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
		     date = sdf.parse(sDate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param  需要格式话的日期
	 * @return 返回“yyyy-MM-dd HH:mm:ss”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateParse(Date d){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sdate = sdf.format(d);
		    date = sdf.parse(sdate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param  需要格式话的日期
	 * @return 返回“yyyy-MM-dd”格式的日期
	 * @author frank.zhu
	 */
	public static Date dateFormatymd(Date d){
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = sdf.format(d);
		    date = sdf.parse(sdate);
		} catch (Exception e) {
			return null;
		}
		return date;
	}
	
	/**
	 * @param  需要格式话的日期
	 * @return 返回“yyyyMMddHHmmss”格式的字符串（用于需要使用时间串作为唯一性字段时）
	 * @author frank.zhu
	 */
	public static String dateParseToString(Date d){
		String sdate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdate = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return sdate;
	}
	/**
	 * @param  需要格式话的日期
	 * @return 返回“yyyy-MM-dd”格式的字符串
	 * @author frank.zhu
	 */
	public static String dateParseyMdToString(Date d){
		String sdate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdate = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return sdate;
	}
	
	/**
	 * @param  需要格式话的日期
	 * @return 返回“HH:mm:ss”格式的字符串
	 * @author frank.zhu
	 */
	public static String dateParseHmsToString(Date d){
		String sdate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			sdate = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return sdate;
	}
	
	/**
	 * @param  需要格式话的日期
	 * @return 返回“yyyy-MM-dd HH:mm:ss”格式的字符串
	 * @author frank.zhu
	 */
	public static String dateParseString(Date d){
		String sdate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdate = sdf.format(d);
		} catch (Exception e) {
			return null;
		}
		return sdate;
	}
	/**
	 * 
	 * @param date 需要操作的日期
	 * @param pam 变化的年数：负数时就是日期往前；正数时就是日期往后
	 * @return 根据年数参数对日期做加或者减之后的日期
	 * @author sugar.lu
	 */
	public static Date addOrMinusYear(Date date,Integer pam){
		Date rtn = new Date(); 
		try {
			GregorianCalendar cal = new GregorianCalendar();  
		    cal.setTime(date);  
		    cal.add(1, pam);  
		    rtn = cal.getTime(); 
		}catch (Exception e) {
			return null;
		}
	    return rtn;  
	}
	/**
	 * 
	 * @param date 需要操作的日期
	 * @param pam 变化的月数：负数时就是日期往前；正数时就是日期往后
	 * @return 根据月数参数对日期做加或者减之后的日期
	 * @author sugar.lu
	 */
	public static Date addOrMinusMonth(Date date,Integer pam){
		Date rtn = new Date(); 
		try {
			GregorianCalendar cal = new GregorianCalendar();  
		    cal.setTime(date);  
		    cal.add(2, pam);  
		    rtn = cal.getTime(); 
		}catch (Exception e) {
			return null;
		}
	    return rtn;  
	}
	/**
	 * 
	 * @param date 需要操作的日期
	 * @param pam 变化的天数：负数时就是日期往前；正数时就是日期往后
	 * @return 根据天数参数对日期做加或者减之后的日期
	 * @author sugar.lu
	 */
	public static Date addOrMinusDay(Date date,Integer pam){
		Date rtn = new Date(); 
		try {
			GregorianCalendar cal = new GregorianCalendar();  
		    cal.setTime(date);  
		    cal.add(5, pam);  
		    rtn = cal.getTime(); 
		}catch (Exception e) {
			return null;
		}
	    return rtn;  
	}
	/**
	 * 
	 * @param date 需要操作的日期
	 * @param pam 变化的小时数：负数时就是日期往前；正数时就是日期往后
	 * @return 根据小时参数对日期做加或者减之后的日期
	 * @author sugar.lu
	 */
	public static Date addOrMinusHour(Date date,Integer pam){
		Date rtn = new Date(); 
		try {
			GregorianCalendar cal = new GregorianCalendar();  
		    cal.setTime(date);  
		    cal.add(10, pam);  
		    rtn = cal.getTime(); 
		}catch (Exception e) {
			return null;
		}
	    return rtn;  
	}
	/**
	 * 
	 * @param date 需要操作的日期
	 * @param pam 变化的分钟数：负数时就是日期往前；正数时就是日期往后
	 * @return 根据分钟参数对日期做加或者减之后的日期
	 * @author sugar.lu
	 */
	public static Date addOrMinusMinute(Date date,Integer pam){
		Date rtn = new Date(); 
		try {
			GregorianCalendar cal = new GregorianCalendar();  
		    cal.setTime(date);  
		    cal.add(12, pam);  
		    rtn = cal.getTime(); 
		}catch (Exception e) {
			return null;
		}
	    return rtn;  
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days));           
    }  
    
    /**
     * 
     * @param date 传入的时间
     * @return  返回传入时间的所属年份
     * @throws ParseException
     */
    public static int getYear(Date date)    
    {    
        Calendar cal = Calendar.getInstance();    
        cal.setTime(date);    
       return cal.get(Calendar.YEAR);           
    } 
    
    /**
     * 
     * @param date 传入的时间
     * @return  返回传入时间的所属季度
     * @throws ParseException
     */
    public static int getQuarter(Date date)  
    {    
        Calendar cal = Calendar.getInstance();    
        cal.setTime(date); 
        int month = cal.get(Calendar.MONTH);
        if(month<3){
        	return 1;
        }else if(month<6){
        	return 2;
        }else if(month<9){
        	return 3;
        }else{
        	return 4;
        }
    } 
}

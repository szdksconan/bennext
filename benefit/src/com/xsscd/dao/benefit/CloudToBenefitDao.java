package com.xsscd.dao.benefit;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
public class CloudToBenefitDao {
    public static CloudToBenefitDao dao = new CloudToBenefitDao();

    /**
     * cardBin数据拉取接口
     */
    public List<Record> findCardBinList(){
        StringBuilder sql = new StringBuilder("select * from card_bin_info");
        return Db.find(sql.toString());
    }

    /**
     * 卡级别对应权益类型拉取接口
     * @return
     */
    public List<Record> findRightsCountList(){
        StringBuilder sql = new StringBuilder("select RightID as rightsId,rank,count from rightcount where RightID in (35,36,37)");
        return Db.find(sql.toString());
    }

    /**
     * 停车场信息拉取接口
     * @return
     */
    public List<Record> getSupplyerInfoList(){
        StringBuilder sql = new StringBuilder("select supplyerName as parkName,supplyerId as clientId from supplyerinfo");
        return Db.find(sql.toString());
    }


    /**
     * 商户对应权益信息拉取接口
     * @return
     */
    public List<Record> getSupplyerRightsList(){
        StringBuilder sql = new StringBuilder( "SELECT b.NeedMoney as price,a.supplyerId as supplyId,c.supplyerName as supplyName,b.RightID as rights,b.Display as display,b.SelfRule1 as selfRule1 FROM service_supplyerinfo a,rightinfo b,supplyerinfo c WHERE a.serviceId = b.ServiceID AND c.supplyerId = a.supplyerId AND b.RightID in(35,36,37)");
        return Db.find(sql.toString());
    }

   /**
     * 卡对应权益信息拉取
     * @return
     */
    public List<Record> getCardRightsList(){
        StringBuilder sql = new StringBuilder( "select a.CardNum as cardNo,a.RightID as rightsType,a.Count as rightsCount,a.PayRule as payRule  from cardright a where   a.RightID in (35,36,37)"  );
        return Db.find(sql.toString());
    }

    /**
     * 查询卡片信息
     */
    public Record getCardRightsByFilter(String cardNo,String rightsType){
        StringBuilder sql = new StringBuilder( "select a.CardNum as cardNo,a.RightID as rightsType,a.Count as rightsCount,a.PayRule as payRule  from cardright a where 1=1 and a.cardNum='"+cardNo+"' and a.RightID = '"+rightsType+"'"  );
        return Db.findFirst(sql.toString());
    }

    /**
     * 更新记录
     */
    public void updateCardRights(String cardNo,String rightsType,int rightsCount){
        StringBuilder sql = new StringBuilder( "update cardright a  set Count = "+rightsCount+"  where 1=1 and a.CardNum='"+cardNo+"' and a.RightID = '"+rightsType+"'"  );
         Db.update(sql.toString());
    }

    public Record getRankByCardBin(String cardBin){
        StringBuilder sql = new StringBuilder(" SELECT cardRank,bankCode from card_bin_info  where cardBin= LEFT('"+cardBin+"', Length(cardBin))  and SUBSTRING('"+cardBin+"',cardStart,rangLength) >= cardBeginRange  and SUBSTRING('"+cardBin+"',cardStart,rangLength) <= cardEndRange"  );
        return Db.findFirst(sql.toString());
    }

    public Record getPayRule(int rightsType){
        StringBuilder sql = new StringBuilder(" SELECT SelfRule1 as PayRule,DailyNumTop as DailyNum,DailyNumPerStore,MonthNumTop as MonthNum,YearNumTop as YearNum from rightinfo  where RightID="+rightsType  );
        return Db.findFirst(sql.toString());
    }

    public Record getRightsMaxCount(int rank,String rightsType){
        StringBuilder sql = new StringBuilder(" SELECT Count  as rightMaxCount from rightcount  where RightID="+rightsType+" and Rank ="+rank  );
        return Db.findFirst(sql.toString());
    }


    public void addRights(Record record){
//        StringBuilder sql = new StringBuilder(" insert into cardright values ('"+cardNo+"','"+rightsType+"',"+Count+","+PayRule+","+DailyNum+","+DailyNumPerStore+","+DateMark+"," +MonthNum+","+YearNum+")");
         Db.save("cardright",record);
    }


    public void updateRightsInc(String rightsId,String cardNum,int Count,Date UpdateTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(UpdateTime);
        StringBuilder sql = new StringBuilder( "update cardrightinc  set UpdateTime = '"+dateString+"',Count="+Count+" where 1=1 and RightID ='"+rightsId+"' AND CardNum='"+cardNum+"'"  );
        Db.update(sql.toString());
    }

    public void addRightsInc(Record record){
        Db.save("cardrightinc",record);
    }

    public Record getRightsInc(String CardNum,String RightID ){
        StringBuilder sql = new StringBuilder( "select * from cardrightinc where 1=1 and RightID ='"+RightID+"' AND CardNum='"+CardNum+"'");
        return Db.findFirst(sql.toString());
    }

    /**
     * 卡对应权益信息拉取(增量)
     * @return
     */
    public List<Record> getCardRightsIncList(String UpdateTime){
        String updateTimeSql="";
        if(!"".equals(UpdateTime))
            updateTimeSql =  "AND c.UpdateTime >= '"+UpdateTime+"'" ;
        StringBuilder sql = new StringBuilder( "select a.CardNum as cardNo,a.RightID as rightsType,a.Count as rightsCount,c.Count as rightsMaxCount,a.PayRule as payRule  from cardright a,cardrightinc c where  c.RightID = a.RightID AND c.CardNum = a.CardNum  "+updateTimeSql );
        return Db.find(sql.toString());
    }

    /**
     * 判断是否第一次使用
     */
    public boolean findFirstRightsUse(String CardNum){
        StringBuilder sql = new StringBuilder( "select * from cardinfo where CardNum ='"+CardNum+"'" );
        List list = Db.find(sql.toString());
        if(list != null && list.size() >0)
            return false;
        else
            return true;
    }
    /**
     * 写入首次使用记录
     */
    public void addFirstRightsUse(String  CardNum,int CardTypeID,String BankId,String UName,String HolderName){
       Record record = new Record();
        record.set("CardNum",CardNum);
        record.set("CardTypeID",CardTypeID);
        record.set("BankId",BankId);
        record.set("UName",UName);
        record.set("HolderName",HolderName);
        Db.save("cardinfo",record);
    }

    public void addUsingRecord(String TermCode,String MerchantCode,String SupplyerName,String CardNum,String RightID,String Amount,Date RecordTime){
        Record record = new Record();
        SimpleDateFormat sdf =   new SimpleDateFormat( "MMdd" );
        String date =  sdf.format(new Date());
        SimpleDateFormat sdf2 =   new SimpleDateFormat( "HHmmss" );
        String date2 =  sdf2.format(new Date());
        record.set("TermCode",TermCode);
        record.set("MerchantCode",MerchantCode);
        record.set("SupplyerName",SupplyerName);
        record.set("CardNum",CardNum);
        record.set("RightID",RightID);
        record.set("Amount",Amount);
        record.set("RecordTime",RecordTime);
        record.set("SysTrackNum","");
        record.set("RetRefNum","");
        record.set("AcceptCode","48267000");
        record.set("SenderCode","M0000165");
        record.set("LiquiDate",date);
        record.set("LocalTransDate",date);
        record.set("LocalTransTime",date2);
        record.set("State",1);
        record.set("RecordTime",RecordTime);
        Db.save("usingrecord",record);
    }

    /**
     * 得到商户信息
     * @return
     */
    public Record getSupplyer(String supplyerId,String supplyerName){
        StringBuilder sql = new StringBuilder( "select merchantCode from supplyerinfo  where supplyerId="+supplyerId+" AND supplyerName ='"+supplyerName+"'" );
        return Db.findFirst(sql.toString());
    }
}

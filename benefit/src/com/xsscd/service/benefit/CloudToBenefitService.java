package com.xsscd.service.benefit;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.xsscd.base.BaseService;
import com.xsscd.dao.benefit.CloudToBenefitDao;
import com.xsscd.entity.benefit.Bill;
import com.xsscd.vo.ResultVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
public class CloudToBenefitService extends BaseService {
    public static CloudToBenefitService service = new CloudToBenefitService();

    /**
     * cardBin数据拉取接口
     * @param ct
     */
    public void findCardBinList(Controller ct){
        List<Record> recordList = CloudToBenefitDao.dao.findCardBinList();
        ResultVo rv = new ResultVo(true, "查询后台数据成功", recordList, recordList.size());
        ct.renderJson(rv);
    }

    /**
     * 卡级别对应权益类型拉取接口
     * @return
     */
    public void findRightsCountList(Controller ct){
        List<Record> recordList = CloudToBenefitDao.dao.findRightsCountList();
        ResultVo rv = new ResultVo(true, "查询后台数据成功", recordList, recordList.size());
        ct.renderJson(rv);
    }

    /**
     * 停车场信息拉取接口
     * @return
     */
    public void getSupplyerInfoList(Controller ct){
        List<Record> recordList = CloudToBenefitDao.dao.getSupplyerInfoList();
        ResultVo rv = new ResultVo(true, "查询后台数据成功", recordList, recordList.size());
        ct.renderJson(rv);
    }


    /**
     * 商户对应权益拉取
     * @return
     */
    public void getSupplyerRightsList(Controller ct){
        List<Record> recordList = CloudToBenefitDao.dao.getSupplyerRightsList();
        ResultVo rv = new ResultVo(true, "查询后台数据成功", recordList, recordList.size());
        ct.renderJson(rv);
    }

    /**
     * 卡对应权益拉取(增量)
     * @return
     */
    public void getCardRightsIncList_no(Controller ct,String UpdateTime){
        try {
            List<Record> recordList = CloudToBenefitDao.dao.getCardRightsIncList(UpdateTime);
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            String lastUpdateTime = sdf.format(new Date());
            ResultVo rv = new ResultVo(true, lastUpdateTime, recordList, recordList.size());
            ct.renderJson(rv);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 卡对应权益拉取(全量)
     * @return
     */
    public void getCardRightsList(Controller ct){
        List<Record> recordListOut = new ArrayList();
        List<Record> recordList = CloudToBenefitDao.dao.getCardRightsList();
        for(Record record:recordList){
            String cardNo = record.getStr("cardNo");
            Record record1 = CloudToBenefitDao.dao.getRankByCardBin(cardNo);
            int cardRank = -1;
            int Count = 1;
            if(record1 != null)
                cardRank = record1.getInt("cardRank");
            Record record2 = CloudToBenefitDao.dao.getRightsMaxCount(cardRank,record.getStr("rightsType"));
            if(record2 != null)
                Count = record2.getInt("rightMaxCount");
            record.set("rightsMaxCount",Count);
            recordListOut.add(record);
        }
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String lastUpdateTime = sdf.format(new Date());
        ResultVo rv = new ResultVo(true, lastUpdateTime, recordListOut, recordListOut.size());
        ct.renderJson(rv);
    }

    public void useCardRights(Controller ct, List<Bill> list){
        //判断是否存在
        Bill billerro = new Bill();
        List<Bill> bills = new ArrayList<Bill>();
        try{
            for(Bill bill : list){
                billerro = bill;
                String cardNo = bill.getCardNo();
                String rightsType = bill.getRightsType();
                Record record = CloudToBenefitDao.dao.getCardRightsByFilter(cardNo,rightsType);
                //存在记录
                if(record != null) {
                    int rightsCount = record.getInt("rightsCount");
                    if (rightsCount > 0) {
                        rightsCount = rightsCount - 1;
                        //更新记录
                        CloudToBenefitDao.dao.updateCardRights(cardNo, rightsType, rightsCount);
                        Record record1 = CloudToBenefitDao.dao.getRankByCardBin(cardNo);
                        int cardRank = -1;
                        if(record1 != null)
                            cardRank = record1.getInt("cardRank");
                        Record record2 =  CloudToBenefitDao.dao.getRightsMaxCount(cardRank,rightsType);
                        int Count = 1;
                        if(record2 != null)
                            Count = record2.getInt("rightMaxCount");
                        addRightsInc(cardNo,rightsType,Count);
                        //写入流水记录
                        int  price =(int) (Double.valueOf( bill.getTotalPrice())*100) ;
                        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
                        //得到商户号
                        Date updateTime = sdf.parse(bill.getCreateTime());
                        Record supplyRecord = getSupplyer(bill.getParkId(),bill.getParkName());
                        addUsingRecord("",supplyRecord.getStr("merchantCode"),bill.getParkName(),cardNo,rightsType,String.valueOf(price) ,updateTime);
                    }
                    else{
                        bills.add(bill);
                    }
                } else{
                        //新增一条记录信息
                        //查询卡级别
                        Record record2 = CloudToBenefitDao.dao.getRankByCardBin(cardNo);
                        if(record2 != null){
                            //查询该卡该权益的最大使用次数和使用规则
                            Record record3 = CloudToBenefitDao.dao.getPayRule(Integer.valueOf(rightsType));
                            Record record4 = CloudToBenefitDao.dao.getRightsMaxCount(record2.getInt("cardRank"),rightsType);
                            //插入一条新纪录
                            SimpleDateFormat formater = new SimpleDateFormat();
                            formater.applyPattern("yyyyMMdd");
                            String DateMark = formater.format(new Date());
                            Record recordinsert = new Record();
                            recordinsert.set("CardNum",cardNo);
                            recordinsert.set("RightID",rightsType);
                            recordinsert.set("Count",record4.getInt("rightMaxCount")-1);
                            recordinsert.set("PayRule",record3.getInt("PayRule"));
                            recordinsert.set("DailyNum",record3.getInt("DailyNum"));
                            recordinsert.set("DailyNumPerStore",record3.getInt("DailyNumPerStore"));
                            recordinsert.set("DateMark",Integer.valueOf(DateMark));
                            recordinsert.set("MonthNum",record3.getInt("MonthNum"));
                            recordinsert.set("YearNum",record3.getInt("YearNum"));
                            CloudToBenefitDao.dao.addRights(recordinsert);
                            addRightsInc(cardNo,rightsType,record4.getInt("rightMaxCount"));
                            //写入首次使用记录
                            addFirstRightsUse(cardNo,record2.getInt("cardRank"),record2.getStr("bankCode"),"","");
                            //写入流水记录
                            int  price =(int) (Double.valueOf( bill.getTotalPrice())*100) ;
                            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
                            //得到商户号
                            Date updateTime = sdf.parse(bill.getCreateTime());
                            Record supplyRecord = getSupplyer(bill.getParkId(),bill.getParkName());
                            addUsingRecord("",supplyRecord.getStr("merchantCode"),bill.getParkName(),cardNo,rightsType,String.valueOf(price) ,updateTime);
                        }
                        else {
                            bills.add(bill);
                        }
                    }
            }
//            if(bills != null && bills.size() > 0){
//                ResultVo rv = new ResultVo(true, "没有可用权益次数",bills,bills.size());
//                ct.renderJson(rv);
//            }
//            else{
                ResultVo rv = new ResultVo(true, "处理成功",bills,bills.size());
                ct.renderJson(rv);
//            }
        }catch (Exception e){
            e.printStackTrace();
            ResultVo rv = new ResultVo(false, "系统异常"+e.getMessage());
            ct.renderJson(rv);
        }
    }

    public void addRightsInc(String cardNum,String rightsId,int Count){
            //查询记录是否存在
            Record record =  CloudToBenefitDao.dao.getRightsInc(cardNum,rightsId);
            if(record == null){
                Record record1 = new Record();
                record1.set("RightID",rightsId);
                record1.set("CardNum",cardNum);
                record1.set("UpdateTime",new Date());
                record1.set("Count",Count);
                CloudToBenefitDao.dao.addRightsInc(record1);
            }
            else{
                CloudToBenefitDao.dao.updateRightsInc(rightsId,cardNum,Count,new Date());

            }
    }
    public void addFirstRightsUse(String  CardNum,int CardTypeID,String BankId,String UName,String HolderName){
            boolean tag = CloudToBenefitDao.dao.findFirstRightsUse(CardNum);
            if(tag)
                CloudToBenefitDao.dao.addFirstRightsUse(CardNum,CardTypeID,BankId,UName,HolderName);
    }

    public void addUsingRecord(String TermCode,String MerchantCode,String SupplyerName,String CardNum,String RightID,String Amount,Date RecordTime){
        CloudToBenefitDao.dao.addUsingRecord( TermCode, MerchantCode, SupplyerName, CardNum, RightID, Amount, RecordTime);
    }

    public  Record getSupplyer(String supplyerId,String supplyerName){
        return  CloudToBenefitDao.dao.getSupplyer(supplyerId,supplyerName);
    }

}

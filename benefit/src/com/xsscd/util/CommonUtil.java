package com.xsscd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class CommonUtil {

	public static int getCardTypeAndBankIDByCardNum(String cardNum,List<Record> cardBins){
		List<Integer> ranks = new ArrayList<Integer>();
		if(cardBins != null){
			for(Record rd:cardBins){
				String tmp = cardNum.substring(rd.getInt("cardStart")-1, rd.getInt("cardStart")+rd.getInt("rangLength")-1);
				String cardBeginRange = rd.getStr("cardBeginRange").trim();
				String cardEndRange = rd.getStr("cardEndRange").trim();
				boolean compareBegin = tmp.compareTo(cardBeginRange)>0||tmp.compareTo(cardBeginRange)==0;
				boolean compareEnd = tmp.compareTo(cardEndRange)<0||tmp.compareTo(cardEndRange)==0;
				if(compareBegin&&compareEnd){
//					System.out.println(rd.getInt("cardRank"));
					ranks.add(rd.getInt("cardRank"));
				}
			}
			Integer rank  = 0;
			if(ranks.size() >0){
				rank = Collections.max(ranks);
			}
			return rank;
		}else{
			return -1;
		}
		
		
		
	}
}

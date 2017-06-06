package com.unipay.benext.model.entity;

import java.io.Serializable;

/**
 * 卡bin
 * Created by Administrator on 2017/2/18.
 */
public class CardBin implements Serializable{
    private static final long serialVersionUID = -1L;

    private int id;
    private String cardBin;//
    private String cardClass;//卡种
    private String bankName;//银行
    private String bankCode;//银行代码
    private String cardName;//卡名称
    private Integer cardRank;//卡级别
    private Integer cardStart;//卡号第几位开始，用于截取卡号来验证卡级别
    private Integer rangLength;//截取卡号时取多长
    private String cardBeginRange;//验证起始数值
    private String cardEndRange;//验证结束数值
    private String isSynTag;//是否需要同步到终端表 0-否 1-是
    private String updateTime;//修改时间
    private String createTime;//创建时间

    private String lastUpdateTime;//最近的一次更新时间
    private int updateType;//更新方式1:增量更新 2:全部更新

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCardBin() {
        return cardBin;
    }
    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getCardClass() {
        return cardClass;
    }
    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }

    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardRank() {
        return cardRank;
    }
    public void setCardRank(Integer cardRank) {
        this.cardRank = cardRank;
    }

    public Integer getCardStart() {
        return cardStart;
    }
    public void setCardStart(Integer cardStart) {
        this.cardStart = cardStart;
    }

    public Integer getRangLength() {
        return rangLength;
    }
    public void setRangLength(Integer rangLength) {
        this.rangLength = rangLength;
    }

    public String getCardBeginRange() {
        return cardBeginRange;
    }
    public void setCardBeginRange(String cardBeginRange) {
        this.cardBeginRange = cardBeginRange;
    }

    public String getCardEndRange() {
        return cardEndRange;
    }
    public void setCardEndRange(String cardEndRange) {
        this.cardEndRange = cardEndRange;
    }

    public String getIsSynTag() {
        return isSynTag;
    }
    public void setIsSynTag(String isSynTag) {
        this.isSynTag = isSynTag;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getUpdateType() {
        return updateType;
    }
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
}

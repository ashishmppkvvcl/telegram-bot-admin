package com.mppkvvcl.telegrambotadmin.dto;

import java.math.BigDecimal;

public class BillSummary {
    private String locationCode;

    private String groupNo;

    private String readingDiaryNo;

    private String consumerNo;

    private String billMonth;

    private String consumerName;

    private String addressOne;

    private String addressTwo;

    private String addressThree;

    private String mobileNo;

    private String billDate;

    private String dueDate;

    private String chequeDueDate;

    private BigDecimal netBill;

    private BigDecimal surcharge;

    private BigDecimal netBillWithSurcharge;

    private BigDecimal sanctionedLoad;

    private String sanctionedLoadUnit;

    private BigDecimal contractDemand;

    private String contractDemandUnit;

    private String phase;

    public BillSummary() {
    }

    public BillSummary(String locationCode, String groupNo, String readingDiaryNo, String consumerNo, String billMonth, String consumerName, String addressOne, String addressTwo, String addressThree, String mobileNo, String billDate, String dueDate, String chequeDueDate, BigDecimal netBill, BigDecimal surcharge, BigDecimal netBillWithSurcharge, BigDecimal sanctionedLoad, String sanctionedLoadUnit, BigDecimal contractDemand, String contractDemandUnit, String phase) {
        this.locationCode = locationCode;
        this.groupNo = groupNo;
        this.readingDiaryNo = readingDiaryNo;
        this.consumerNo = consumerNo;
        this.billMonth = billMonth;
        this.consumerName = consumerName;
        this.addressOne = addressOne;
        this.addressTwo = addressTwo;
        this.addressThree = addressThree;
        this.mobileNo = mobileNo;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.chequeDueDate = chequeDueDate;
        this.netBill = netBill;
        this.surcharge = surcharge;
        this.netBillWithSurcharge = netBillWithSurcharge;
        this.sanctionedLoad = sanctionedLoad;
        this.sanctionedLoadUnit = sanctionedLoadUnit;
        this.contractDemand = contractDemand;
        this.contractDemandUnit = contractDemandUnit;
        this.phase = phase;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getReadingDiaryNo() {
        return readingDiaryNo;
    }

    public void setReadingDiaryNo(String readingDiaryNo) {
        this.readingDiaryNo = readingDiaryNo;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getAddressThree() {
        return addressThree;
    }

    public void setAddressThree(String addressThree) {
        this.addressThree = addressThree;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getChequeDueDate() {
        return chequeDueDate;
    }

    public void setChequeDueDate(String chequeDueDate) {
        this.chequeDueDate = chequeDueDate;
    }

    public BigDecimal getNetBill() {
        return netBill;
    }

    public void setNetBill(BigDecimal netBill) {
        this.netBill = netBill;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(BigDecimal surcharge) {
        this.surcharge = surcharge;
    }

    public BigDecimal getNetBillWithSurcharge() {
        return netBillWithSurcharge;
    }

    public void setNetBillWithSurcharge(BigDecimal netBillWithSurcharge) {
        this.netBillWithSurcharge = netBillWithSurcharge;
    }

    public BigDecimal getSanctionedLoad() {
        return sanctionedLoad;
    }

    public void setSanctionedLoad(BigDecimal sanctionedLoad) {
        this.sanctionedLoad = sanctionedLoad;
    }

    public String getSanctionedLoadUnit() {
        return sanctionedLoadUnit;
    }

    public void setSanctionedLoadUnit(String sanctionedLoadUnit) {
        this.sanctionedLoadUnit = sanctionedLoadUnit;
    }

    public BigDecimal getContractDemand() {
        return contractDemand;
    }

    public void setContractDemand(BigDecimal contractDemand) {
        this.contractDemand = contractDemand;
    }

    public String getContractDemandUnit() {
        return contractDemandUnit;
    }

    public void setContractDemandUnit(String contractDemandUnit) {
        this.contractDemandUnit = contractDemandUnit;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "locationCode='" + locationCode + '\'' +
                ", groupNo='" + groupNo + '\'' +
                ", readingDiaryNo='" + readingDiaryNo + '\'' +
                ", consumerNo='" + consumerNo + '\'' +
                ", billMonth='" + billMonth + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", addressOne='" + addressOne + '\'' +
                ", addressTwo='" + addressTwo + '\'' +
                ", addressThree='" + addressThree + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", billDate='" + billDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", chequeDueDate='" + chequeDueDate + '\'' +
                ", netBill=" + netBill +
                ", surcharge=" + surcharge +
                ", netBillWithSurcharge=" + netBillWithSurcharge +
                ", sanctionedLoad=" + sanctionedLoad +
                ", sanctionedLoadUnit='" + sanctionedLoadUnit + '\'' +
                ", contractDemand=" + contractDemand +
                ", contractDemandUnit='" + contractDemandUnit + '\'' +
                ", phase='" + phase + '\'' +
                '}';
    }
}

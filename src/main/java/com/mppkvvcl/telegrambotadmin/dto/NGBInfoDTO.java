package com.mppkvvcl.telegrambotadmin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NGBInfoDTO {
    @JsonProperty("MONTH_BILL")
    private Double MONTH_BILL;

    @JsonProperty("ARRS")
    private Double ARRS;

    @JsonProperty("CONS_NAME_1")
    private String CONS_NAME_1;

    @JsonProperty("ADDR_1")
    private String ADDR_1;

    @JsonProperty("ADDR_2")
    private String ADDR_2;

    @JsonProperty("ADDR_3")
    private String ADDR_3;

    @JsonProperty("MOBILE")
    private String MOBILE;

    @JsonProperty("CIR_NAME_2")
    private String CIR_NAME_2;

    @JsonProperty("DIV_NAME")
    private String DIV_NAME;

    @JsonProperty("LOC_NAME")
    private String LOC_NAME;

    @JsonProperty("TARIFF")
    private String TARIFF;

    @JsonProperty("GROUP_NO_1")
    private String GROUP_NO_1;

    @JsonProperty("RDG_CURR")
    private String RDG_CURR;

    @JsonProperty("RDG_DATE")
    private String RDG_DATE;

    @JsonProperty("BILL_ID")
    private String BILL_ID;

    @JsonProperty("BILL_DATE")
    private String BILL_DATE;

    @JsonProperty ("BILL_MONTH_1")
    private String BILL_MONTH_1;

    @JsonProperty("CSH_DATE_2")
    private String CSH_DATE_2;

    @JsonProperty("CONS_NO_1")
    private String CONS_NO_1;

    public Double getMONTH_BILL() {
        return MONTH_BILL;
    }

    public void setMONTH_BILL(Double MONTH_BILL) {
        this.MONTH_BILL = MONTH_BILL;
    }

    public Double getARRS() {
        return ARRS;
    }

    public void setARRS(Double ARRS) {
        this.ARRS = ARRS;
    }

    public String getCONS_NAME_1() {
        return CONS_NAME_1;
    }

    public void setCONS_NAME_1(String CONS_NAME_1) {
        this.CONS_NAME_1 = CONS_NAME_1;
    }

    public String getADDR_1() {
        return ADDR_1;
    }

    public void setADDR_1(String ADDR_1) {
        this.ADDR_1 = ADDR_1;
    }

    public String getADDR_2() {
        return ADDR_2;
    }

    public void setADDR_2(String ADDR_2) {
        this.ADDR_2 = ADDR_2;
    }

    public String getADDR_3() {
        return ADDR_3;
    }

    public void setADDR_3(String ADDR_3) {
        this.ADDR_3 = ADDR_3;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getCIR_NAME_2() {
        return CIR_NAME_2;
    }

    public void setCIR_NAME_2(String CIR_NAME_2) {
        this.CIR_NAME_2 = CIR_NAME_2;
    }

    public String getDIV_NAME() {
        return DIV_NAME;
    }

    public void setDIV_NAME(String DIV_NAME) {
        this.DIV_NAME = DIV_NAME;
    }

    public String getLOC_NAME() {
        return LOC_NAME;
    }

    public void setLOC_NAME(String LOC_NAME) {
        this.LOC_NAME = LOC_NAME;
    }

    public String getTARIFF() {
        return TARIFF;
    }

    public void setTARIFF(String TARIFF) {
        this.TARIFF = TARIFF;
    }

    public String getGROUP_NO_1() {
        return GROUP_NO_1;
    }

    public void setGROUP_NO_1(String GROUP_NO_1) {
        this.GROUP_NO_1 = GROUP_NO_1;
    }

    public String getRDG_CURR() {
        return RDG_CURR;
    }

    public void setRDG_CURR(String RDG_CURR) {
        this.RDG_CURR = RDG_CURR;
    }

    public String getRDG_DATE() {
        return RDG_DATE;
    }

    public void setRDG_DATE(String RDG_DATE) {
        this.RDG_DATE = RDG_DATE;
    }

    public String getBILL_ID() {
        return BILL_ID;
    }

    public void setBILL_ID(String BILL_ID) {
        this.BILL_ID = BILL_ID;
    }

    public String getBILL_DATE() {
        return BILL_DATE;
    }

    public void setBILL_DATE(String BILL_DATE) {
        this.BILL_DATE = BILL_DATE;
    }

    public String getBILL_MONTH_1() {
        return BILL_MONTH_1;
    }

    public void setBILL_MONTH_1(String BILL_MONTH_1) {
        this.BILL_MONTH_1 = BILL_MONTH_1;
    }

    public String getCSH_DATE_2() {
        return CSH_DATE_2;
    }

    public void setCSH_DATE_2(String CSH_DATE_2) {
        this.CSH_DATE_2 = CSH_DATE_2;
    }

    public String getCONS_NO_1() {
        return CONS_NO_1;
    }

    public void setCONS_NO_1(String CONS_NO_1) {
        this.CONS_NO_1 = CONS_NO_1;
    }

    @Override
    public String toString() {
        return "NGBInfoDTO{" +
                "MONTH_BILL=" + MONTH_BILL +
                ", ARRS=" + ARRS +
                ", CONS_NAME_1='" + CONS_NAME_1 + '\'' +
                ", ADDR_1='" + ADDR_1 + '\'' +
                ", ADDR_2='" + ADDR_2 + '\'' +
                ", ADDR_3='" + ADDR_3 + '\'' +
                ", MOBILE='" + MOBILE + '\'' +
                ", CIR_NAME_2='" + CIR_NAME_2 + '\'' +
                ", DIV_NAME='" + DIV_NAME + '\'' +
                ", LOC_NAME='" + LOC_NAME + '\'' +
                ", TARIFF='" + TARIFF + '\'' +
                ", GROUP_NO_1='" + GROUP_NO_1 + '\'' +
                ", RDG_CURR='" + RDG_CURR + '\'' +
                ", RDG_DATE='" + RDG_DATE + '\'' +
                ", BILL_ID='" + BILL_ID + '\'' +
                ", BILL_DATE='" + BILL_DATE + '\'' +
                ", BILL_MONTH_1='" + BILL_MONTH_1 + '\'' +
                ", CSH_DATE_2='" + CSH_DATE_2 + '\'' +
                ", CONS_NO_1='" + CONS_NO_1 + '\'' +
                '}';
    }
}

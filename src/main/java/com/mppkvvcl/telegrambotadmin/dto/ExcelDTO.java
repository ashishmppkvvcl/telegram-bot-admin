package com.mppkvvcl.telegrambotadmin.dto;

public class ExcelDTO {
   private String consumerNo;
   private String billMonth;


    public ExcelDTO() {
    }

    public ExcelDTO(String consumerNo, String billMonth) {
        this.consumerNo = consumerNo;
        this.billMonth = billMonth;
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

    @Override
    public String toString() {
        return "YojnaDTO{" +
                "consumerNo='" + consumerNo + '\'' +
                ", billMonth='" + billMonth + '\'' +
                '}';
    }
}

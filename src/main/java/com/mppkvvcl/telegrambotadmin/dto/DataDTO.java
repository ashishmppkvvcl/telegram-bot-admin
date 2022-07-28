package com.mppkvvcl.telegrambotadmin.dto;

import java.util.Arrays;

public class DataDTO {

    byte [] data;
    String chatId;
    String FileName;

    public DataDTO() {
    }

    public DataDTO(byte[] data, String chatId, String fileName) {
        this.data = data;
        this.chatId = chatId;
        FileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    @Override
    public String toString() {
        return "DataDTO{" +
                "data=" + Arrays.toString(data) +
                ", chatId='" + chatId + '\'' +
                ", FileName='" + FileName + '\'' +
                '}';
    }
}

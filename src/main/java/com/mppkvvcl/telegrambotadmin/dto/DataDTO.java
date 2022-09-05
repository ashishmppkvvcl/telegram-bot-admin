package com.mppkvvcl.telegrambotadmin.dto;

import java.util.Arrays;

public class DataDTO {

    byte [] data;
    String folderName;
    String fileName;

    public DataDTO() {
    }

    public DataDTO(byte[] data, String folderName, String fileName) {
        this.data = data;
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DataDTO{" +
                "data=" + Arrays.toString(data) +
                ", folderName='" + folderName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}

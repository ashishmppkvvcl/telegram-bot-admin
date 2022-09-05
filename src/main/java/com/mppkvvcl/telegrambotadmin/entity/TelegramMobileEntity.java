package com.mppkvvcl.telegrambotadmin.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

@Document(collection = "telegram_mobile")
public class TelegramMobileEntity extends BaseEntity {
    @Id
    private String id;

    @NotNull(message = "User ID can not be null")
    @Indexed(unique = true)
    private String chatID;

    private String mobileNo;

    private Update update;


    public TelegramMobileEntity() {
    }

    public TelegramMobileEntity(@NotNull(message = "User ID can not be null") String chatID, String mobileNo) {
        this.chatID = chatID;
        this.mobileNo = mobileNo;
    }

    public TelegramMobileEntity(String id, @NotNull(message = "User ID can not be null") String chatID, String mobileNo) {
        this.id = id;
        this.chatID = chatID;
        this.mobileNo = mobileNo;
    }

    public TelegramMobileEntity(@NotNull(message = "User ID can not be null") String chatID, String mobileNo, Update update) {
        this.chatID = chatID;
        this.mobileNo = mobileNo;
        this.update = update;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "TelegramMobileEntity{" +
                "id='" + id + '\'' +
                ", chatID='" + chatID + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", update=" + update +
                '}';
    }
}

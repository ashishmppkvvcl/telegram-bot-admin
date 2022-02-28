package com.mppkvvcl.telegrambotadmin.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.validation.constraints.NotNull;

@Document(collection = "telegram_mobile")
public class TelegramMobileEntity extends BaseEntity {
    @Id
    private String id;

    @NotNull(message = "User ID can not be null")
    @Indexed(unique = true)
    private String chatID;

    private String mobileNo;

    private User user;


    public TelegramMobileEntity() {
    }

    public TelegramMobileEntity(@NotNull(message = "User ID can not be null") String chatID, String mobileNo, User user) {
        this.chatID = chatID;
        this.mobileNo = mobileNo;
        this.user = user;
    }

    public TelegramMobileEntity(String id, @NotNull(message = "User ID can not be null") String chatID, String mobileNo, User user) {
        this.id = id;
        this.chatID = chatID;
        this.mobileNo = mobileNo;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TelegramMobileEntity{" +
                "id='" + id + '\'' +
                ", chatID='" + chatID + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", user=" + user +
                '}';
    }
}

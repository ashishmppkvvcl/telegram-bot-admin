package com.mppkvvcl.telegrambotadmin.entity;

import com.mppkvvcl.telegrambotadmin.dto.BillSummary;
import com.mppkvvcl.telegrambotadmin.dto.NGBInfoDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

@Document(collection = "telegram")
public class TelegramEntity extends BaseEntity{
    @Id
    private String id;

    @NotNull(message = "User Id can not be null")
    @Indexed(unique = true)
    private String chatID;

    private String message;

    private NGBInfoDTO ngbInfoDTO;

    private BillSummary billSummary;

    private Update update;

    public TelegramEntity() {
    }

    public TelegramEntity(@NotNull(message = "User Id can not be null") String chatID, String message, BillSummary billSummary, Update update) {
        this.chatID = chatID;
        this.message = message;
        this.billSummary = billSummary;
        this.update = update;
    }

    public TelegramEntity(@NotNull(message = "User Id can not be null") String chatID, String message, NGBInfoDTO ngbInfoDTO, BillSummary billSummary, Update update) {
        this.chatID = chatID;
        this.message = message;
        this.ngbInfoDTO = ngbInfoDTO;
        this.billSummary = billSummary;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NGBInfoDTO getNgbInfoDTO() {
        return ngbInfoDTO;
    }

    public void setNgbInfoDTO(NGBInfoDTO ngbInfoDTO) {
        this.ngbInfoDTO = ngbInfoDTO;
    }

    public BillSummary getBillSummary() {
        return billSummary;
    }

    public void setBillSummary(BillSummary billSummary) {
        this.billSummary = billSummary;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "TelegramEntity{" +
                "id='" + id + '\'' +
                ", chatID='" + chatID + '\'' +
                ", message='" + message + '\'' +
                ", ngbInfoDTO=" + ngbInfoDTO +
                ", billSummary=" + billSummary +
                ", update=" + update +
                '}';
    }
}

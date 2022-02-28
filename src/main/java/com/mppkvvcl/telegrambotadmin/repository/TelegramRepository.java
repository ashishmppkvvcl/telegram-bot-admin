package com.mppkvvcl.telegrambotadmin.repository;

import com.mppkvvcl.telegrambotadmin.entity.TelegramEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("telegramRepository")
public interface TelegramRepository extends MongoRepository<TelegramEntity, String>/*,CrudRepository<LobModel,String>*/, PagingAndSortingRepository<TelegramEntity, String> {

    TelegramEntity findTopByChatIDOrderByIdDesc(String chatID);

}

package com.mppkvvcl.telegrambotadmin.repository;


import com.mppkvvcl.telegrambotadmin.entity.TelegramMobileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("telegramMobileRepository")
public interface TelegramMobileRepository extends MongoRepository<TelegramMobileEntity, String>/*,CrudRepository<LobModel,String>*/, PagingAndSortingRepository<TelegramMobileEntity, String> {
    TelegramMobileEntity findByChatID(String chatID);

    boolean existsByChatID(String chatID);
}

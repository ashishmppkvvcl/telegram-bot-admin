package com.mppkvvcl.telegrambotadmin.service;

import com.mppkvvcl.telegrambotadmin.entity.TelegramMobileEntity;
import com.mppkvvcl.telegrambotadmin.repository.TelegramMobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TelegramMobileService {

   @Autowired
    TelegramMobileRepository telegramMobileRepository;

   public ResponseEntity<?> registerChatID(TelegramMobileEntity telegramMobileEntity) {

       TelegramMobileEntity telegramMobileEntity1 =telegramMobileRepository.save(telegramMobileEntity);
       if(telegramMobileEntity1!=null){
           return new ResponseEntity<TelegramMobileEntity>(telegramMobileEntity1, HttpStatus.OK);
       }
       else
           return new ResponseEntity<>(HttpStatus.CONFLICT);

   }

}

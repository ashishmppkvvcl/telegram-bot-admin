package com.mppkvvcl.telegrambotadmin.controller;

import com.mppkvvcl.telegrambotadmin.botcode.MyAmazingBot;
import com.mppkvvcl.telegrambotadmin.entity.TelegramMobileEntity;
import com.mppkvvcl.telegrambotadmin.repository.TelegramMobileRepository;
import com.mppkvvcl.telegrambotadmin.service.TelegramMobileService;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
public class TelegramMobileController {

    private static final Logger logger = LoggerUtil.getLogger(TelegramMobileController.class);
    private static final String PATTERN = "^[0-9]{10}$";

    @Autowired
    TelegramMobileService telegramMobileService;

    @Autowired
    TelegramMobileRepository telegramMobileRepository;

    @Autowired
    MyAmazingBot myAmazingBot;

    @PostMapping
    public ResponseEntity<?> registerChatID(@RequestBody TelegramMobileEntity telegramMobileEntity) {
        logger.info("Inside TelegramMobileController.registerChatID() method");
        if(telegramMobileEntity!=null&&telegramMobileEntity.getMobileNo()!=null&&telegramMobileEntity.getChatID()!=null)
        {
            if(!telegramMobileEntity.getMobileNo().matches(PATTERN))
            {
                return new ResponseEntity<>("Mobile no. is not 10 digit",HttpStatus.BAD_REQUEST);
            }

            if(telegramMobileEntity.getChatID()==null)
            {
                return new ResponseEntity<>("Chat ID is Blank",HttpStatus.BAD_REQUEST);
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(telegramMobileEntity.getChatID());
            sendMessage.setText("Your mobile no. : "+telegramMobileEntity.getMobileNo()+" registered succcessfully");
            try{
                if(!telegramMobileRepository.existsByChatID(telegramMobileEntity.getChatID()))
            logger.info(myAmazingBot.execute(sendMessage).getText());
            return telegramMobileService.registerChatID(telegramMobileEntity);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
                return new ResponseEntity("Wrong Chat ID",HttpStatus.CONFLICT);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return new ResponseEntity<>("Already Registered",HttpStatus.CONFLICT);
            }

        }
        else
        {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}

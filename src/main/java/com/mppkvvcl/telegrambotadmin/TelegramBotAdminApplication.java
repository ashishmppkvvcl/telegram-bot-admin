package com.mppkvvcl.telegrambotadmin;

import com.mppkvvcl.telegrambotadmin.botcode.MyAmazingBot;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotAdminApplication {
	@Autowired
	MyAmazingBot myAmazingBot;
	private static Logger logger = LoggerUtil.getLogger(TelegramBotAdminApplication.class);

	public static void main(String[] args) {
		logger.info("TelegramBotAdminApplication Started");
		SpringApplication.run(TelegramBotAdminApplication.class, args);
	}

}

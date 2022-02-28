package com.mppkvvcl.telegrambotadmin.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class LoggerUtil {

    public static Logger getLogger(Class className) {
        return LoggerFactory.getLogger(className);
    }
}

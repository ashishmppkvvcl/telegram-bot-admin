package com.mppkvvcl.telegrambotadmin.utility;


import java.util.Map;

public class Static {
    //public static final String CMI_INFO_URL = "http://localhost:8085/api/v1/rest-template/ngb-info?consumerNo="; // Consumer No With N Handled
    public static final String CMI_INFO_URL = "http://localhost:8085/api/v1/rest-template/bill-summary?consumerNo="; // Consumer No With N Handled
    public static final String CMI_PAYMENT_URl = "http://localhost:8085/api/v1/rest-template/ngb-payment?consumerNo=";
    //public static final String CMI_PDF_BILL ="http://localhost:8085/api/v1/rest-template/ngb-bill-download-by-ngb-bill-id?ngb-bill-id=";
    public static final String CMI_PDF_BILL = "http://localhost:8085/api/v1/rest-template/ngb-bill-download-by-bill-month?consumerNo=";
    public static final String CMI_PASSBOOK = "http://localhost:8085/api/v1/rest-template/ngb-passbook-by-consumer-no?consumerNo=";
    public static final String CMI_MOBILE_MAPPING = "http://localhost:8085/api/v1/mobile?primary_mobile_no=";
    public static final String CMI_BILL_MONTHS = "http://localhost:8085/api/v1/rest-template/bill-months?consumerNo=";
    public static final String CHAT_APP_MAPPPING = "https://ngb.mpwin.co.in/mppkvvcl/ngb/report/backend/api/v1/chat-app/add-update/telegram/mobile-no/";
    public static final String prodBotUsername = "mpwzbot";
    public static final String prodBotToken = "5221323138:AAEoW5ms59fvyd1qfWYBGOpGsw_O7OISgFA";
    public static final String testBotUserName = "cmi_test_bot";
    public static final String testBotToken = "5123069626:AAEsqB_0ozfM42302f-53RKyOuvjkZs4C8k";
    public static Map<String, String> env = System.getenv();
    public static String BOT_USERNAME = env.get("BOT_USERNAME_PUBLIC");
    public static String BOT_TOKEN = env.get("BOT_TOKEN_PUBLIC");
    public static String CMI_TOKEN = env.get("CMI_TOKEN_PRODUCTION");
    public static String NGB_TOKEN = env.get("NGB_TOKEN_PRODUCTION");

    private Static() {
    }
}

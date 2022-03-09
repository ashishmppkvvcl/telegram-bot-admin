package com.mppkvvcl.telegrambotadmin.utility;


import java.util.Map;

public class Static {
    private Static() {
    }

    public static Map<String, String> env = System.getenv();
    public static String CMI_TOKEN = env.get("CMI_TOKEN_PRODUCTION");
    public static final String CMI_INFO_URL = "http://localhost:8085/api/v1/rest-template/ngb-info?consumerNo="; // Consumer No With N Handled
    public static final String CMI_PAYMENT_URl = "http://localhost:8085/api/v1/rest-template/ngb-payment?consumerNo=";
    public static final String CMI_PDF_BILL ="http://localhost:8085/api/v1/rest-template/ngb-bill-download-by-ngb-bill-id?ngb-bill-id=";
    public static final String CMI_PASSBOOK = "http://localhost:8085/api/v1/rest-template/ngb-passbook-by-consumer-no?consumerNo=";
    public static final String prodBotUsername = "mpwz_admin_bot";
    public static final String prodBotToken = "5221836920:AAGkriIZKTrIvyQt8RSg1dHyHRpvn32iGJM";
    public static final String testBotUserName = "cmi_test_bot";
    public static final String testBotToken="5123069626:AAEsqB_0ozfM42302f-53RKyOuvjkZs4C8k";
}

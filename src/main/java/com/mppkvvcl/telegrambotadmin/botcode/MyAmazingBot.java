package com.mppkvvcl.telegrambotadmin.botcode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mppkvvcl.telegrambotadmin.dto.BillSummary;
import com.mppkvvcl.telegrambotadmin.entity.TelegramEntity;
import com.mppkvvcl.telegrambotadmin.entity.TelegramMobileEntity;
import com.mppkvvcl.telegrambotadmin.repository.TelegramMobileRepository;
import com.mppkvvcl.telegrambotadmin.repository.TelegramRepository;
import com.mppkvvcl.telegrambotadmin.service.RestTemplateService;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import com.mppkvvcl.telegrambotadmin.utility.Static;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class MyAmazingBot extends TelegramLongPollingBot {
    private static final String PATTERN = "^[0-9]{10}$";
    ObjectMapper mapper = new ObjectMapper();
    private Logger logger = LoggerUtil.getLogger(MyAmazingBot.class);
    @Autowired
    private RestTemplateService restTemplateService;
    @Autowired
    private TelegramRepository telegramRepository;
    @Autowired
    private TelegramMobileRepository telegramMobileRepository;

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SendMessage sendMessage = new SendMessage();
            SendDocument sendDocument = new SendDocument();
            BillSummary billSummary = null;
            String chatId = null;


            if (update.hasMessage()) {
                chatId = update.getMessage().getChatId().toString();
                sendMessage.setChatId(chatId);
            }

            if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                sendMessage.setChatId(chatId);
            }

            TelegramMobileEntity telegramMobileEntity = telegramMobileRepository.findByChatID(chatId);

//=====================================================Normal Process Started ==========================================================
            if (telegramMobileEntity != null) {
                //==========================================Simple Message Start=====================================================//
// We check if the update has a message and the message has text
                if (update.hasMessage() && update.getMessage().hasText()) {
                    String message_text = null;
                    ResponseEntity responseEntity = null;
                    List<Map> consumerMobileMappings = null;
                    message_text = update.getMessage().getText();


                    try {
                        consumerMobileMappings = restTemplateService.getConsumerNoByMobileNo(telegramMobileEntity.getMobileNo());
                    } catch (Exception e) {
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Sorry , Your mobile no : "+telegramMobileEntity.getMobileNo()+" doesn't linked to any consumer no .");
                        execute(sendMessage);
                        sendMessage.setText("Please contact to your zone office for linking your mobile no. to the consumer no");
                        execute(sendMessage);
                        e.printStackTrace();
                        return;
                    }


                    if (message_text.equals("/start")) {
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " !" + "\n");
                        billSummary = null;
                        message_text = null;
                        execute(sendMessage); // Sending our message object to user
                        sendMessage.setReplyMarkup(setConsumerMobileMapping(restTemplateService.getConsumerNoByMobileNo(telegramMobileEntity.getMobileNo())));
                        sendMessage.setText("Please select from below consumers mapped to your mobile no: " + telegramMobileEntity.getMobileNo());
                        execute(sendMessage);
                        return;
                    }

                    if (!message_text.substring(0, 1).equals("3"))
                        message_text = telegramMobileEntity.getMobileNo();


                    boolean found = true;
                    if (message_text.substring(0, 1).equals("3")) {
                        found=false;
                        consumerMobileMappings = restTemplateService.getConsumerNoByMobileNo(telegramMobileEntity.getMobileNo());
                        for (Map consumerMobileMapping : consumerMobileMappings) {
                            if (consumerMobileMapping.containsValue(message_text))
                                found = true;
                        }
                    }

                    if (!found) {
                        sendMessage.setText("You are allowed to select only from below consumers mapped to your mobile no : " + telegramMobileEntity.getMobileNo());
                        sendMessage.setReplyMarkup(setConsumerMobileMapping(restTemplateService.getConsumerNoByMobileNo(telegramMobileEntity.getMobileNo())));
                        execute(sendMessage);
                        return;
                    }

                    if (message_text.matches(PATTERN)) {
                        if (!message_text.substring(0, 1).equals("3")) {
                            List<Map> consumerMobileMapping = null;
                            try {
                                consumerMobileMapping = restTemplateService.getConsumerNoByMobileNo(message_text);
                            } catch (Exception e) {
                                sendMessage.setText("No consumer number found with this mobile number");
                                execute(sendMessage);
                                return;
                            }
                            sendMessage.setReplyMarkup(setConsumerMobileMapping(consumerMobileMapping));
                            if (consumerMobileMapping.size() > 10)
                                sendMessage.setText("Total Consumers Found : " + consumerMobileMapping.size() + "\nPlease select from below top 10 consumers :");
                            else
                                sendMessage.setText("Total Consumers Found : " + consumerMobileMapping.size() + "\nPlease select from below consumers mapped to your mobile no: " + telegramMobileEntity.getMobileNo());
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        //  responseEntity = restTemplateService.getNGBInfo(message_text);
                        responseEntity = restTemplateService.getBillSummary(message_text);
                    } else {
                        sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " !" + "\nPlease enter 10 digit consumer no. or mobile no.");
                        billSummary = null;
                        message_text = null;
                        try {
                            execute(sendMessage); // Sending our message object to user
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }


                    if (responseEntity == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                        String firstName = "";
                        String lastName = "";
                        if (update.getMessage().getFrom() != null) {
                            lastName = update.getMessage().getFrom().getLastName();
                            firstName = update.getMessage().getFrom().getFirstName();
                            if (firstName == null) {
                                firstName = "";
                            }
                            if (lastName == null) {
                                lastName = "";
                            }
                        }
                        sendMessage.setText("Hi " + firstName + " " + lastName + " ! \nThis is wrong consumer number or no bill found for this consumer");
                        billSummary = null;
                        message_text = null;
                        try {
                            execute(sendMessage); // Sending our message object to user
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    //  ngbInfoDTOs = mapper.convertValue(responseEntity.getBody(), new TypeReference<NGBInfoDTO>() {
                    //  });
                    //  ngbInfoDTO = ngbInfoDTOs.get(0);

                    //  ngbInfoDTO= (NGBInfoDTO) responseEntity.getBody();

                    billSummary = mapper.convertValue(responseEntity.getBody(), new TypeReference<BillSummary>() {
                    });


                    TelegramEntity telegramEntity = new TelegramEntity(chatId, message_text, billSummary, update);
                    TelegramEntity telegramEntity1 = telegramRepository.findTopByChatIDOrderByIdDesc(chatId);
                    if (telegramEntity1 != null)
                        telegramEntity.setId(telegramEntity1.getId());
                    telegramRepository.save(telegramEntity);
                    sendMessage.setReplyMarkup(setInline());
                    sendMessage.setText("Please Select From Below Options :");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    InlineKeyboardMarkup markupKeyboard = (InlineKeyboardMarkup) sendMessage.getReplyMarkup();
                    if (markupKeyboard != null)
                        markupKeyboard.getKeyboard().clear();
                    return;
                }
//==========================================Simple Message End=======================================================//


//============================================= CallBack Inline Keyboard ===========================================//
                //From Inline Keyboard Markup CallBack
                if (update.hasCallbackQuery()) {
                    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                    EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
                    TelegramMobileEntity telegramMobileEntity1 = telegramMobileRepository.findByChatID(update.getCallbackQuery().getMessage().getChatId().toString());
                    Double netBill = null;
                    String call_data = update.getCallbackQuery().getData();
                    String chat_id = update.getCallbackQuery().getMessage().getChatId().toString();
                    sendMessage.setChatId(chat_id);
                    TelegramEntity telegramEntity = telegramRepository.findTopByChatIDOrderByIdDesc(chat_id);
                    if (telegramEntity != null) {
                        //ngbInfoDTO = telegramEntity.getNgbInfoDTO();
                        billSummary = telegramEntity.getBillSummary();
                    }
                    if (telegramEntity == null)
                        // ngbInfoDTO = null;
                        billSummary = null;


                    //=======================================If User Reached With Previous Inline Keyboard Started===================================
//                    if(ngbInfoDTO!=null){
//                        if(telegramMobileEntity1 !=null){
//                        if(!telegramMobileEntity1.getMobileNo().equals(ngbInfoDTO.getMOBILE())){
//                            sendMessage.setText("Your mobile no. not matched with consumer no.");
//                            execute(sendMessage);
//                            return;
//                        }
//                        }
//                        else{
//                            sendMessage.setReplyMarkup(setButtons());
//                            sendMessage.setText("Register your mobile no. with chatbot");
//                            return;
//                        }
//                    }
                    //=======================================If User Reached With Previous Inline Keyboard Ended===================================

                    if (call_data.equals("BILL DETAILS") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        sendMessage.setReplyMarkup(setInline());
                        // netBill=Double.sum(billSummary.getMONTH_BILL(),billSummary.getARRS());
                        sendMessage.setText(
                                "Your Details are :\r\n" +
                                        "Consumer No. : " + billSummary.getConsumerNo() + "\n" +
                                        "Consumer Name : " + billSummary.getConsumerName() + "\n" +
                                        "Consumer Address : " + billSummary.getAddressOne() + " " + billSummary.getAddressTwo() + " " + billSummary.getAddressThree() + "\n" +
                                        "Bill Month : " + billSummary.getBillMonth() + "\n" +
                                        // "Read Date :" + billSummary.+ "\n" +
                                        // "Current Read :" + ngbInfoDTO.getRDG_CURR() + "\n" +
                                        "Bill Date :" + billSummary.getBillDate() + "\n" +
                                        // "Current Bill :" + ngbInfoDTO.getMONTH_BILL() + "\n" +
                                        "Net Bill :" + billSummary.getNetBill() + "\n" +
                                        "Due By :" + billSummary.getDueDate()
                        );
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("PDF BILL") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        sendMessage.setReplyMarkup(setCurrentPreviousBills());
                        sendMessage.setText("Please Select :");
                        // logger.info(ngbInfoDTO.getBILL_ID());
                       /* byte[] billPDF = restTemplateService.getNGBBillPDFbyNgbBillId(ngbInfoDTO.getBILL_ID());
                        InputStream myInputStream = new ByteArrayInputStream(billPDF);
                        InputFile inputFile = new InputFile();
                        inputFile.setMedia(myInputStream, ngbInfoDTO.getCONS_NO_1().substring(1) + ".pdf");
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(chat_id); */
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("PAYMENT") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        sendMessage.setReplyMarkup(setInline());
                        List<Map> payments = restTemplateService.getNGBPayment(billSummary.getConsumerNo().substring(1), "payDate", "DESC", 1, 10);
                        if (payments.isEmpty()) {
                            sendMessage.setText("No Payment Found");
                            execute(sendMessage);
                            return;
                        }
                        String CompletePaymentString = "";
                        int i = 1;
                        for (Map payment : payments) {
                            String paymentString = "S.no. " + i + "." + " Pay mode: " + payment.get("payMode") + "  Amount: " + payment.get("amount") + " Pay Date: " + payment.get("payDate") + "  Punching Date: " + payment.get("punchingDate").toString().substring(0, 10) + "  Posting Date: " + payment.get("postingDate") + "\n\n";
                            CompletePaymentString = CompletePaymentString.concat(paymentString);
                            i++;
                        }
                        sendMessage.setText(CompletePaymentString);
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("PASSBOOK") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        // logger.info(ngbInfoDTO.getBILL_ID());
                        byte[] billPDF = restTemplateService.getNGBPassbookByConsumerNo(billSummary.getConsumerNo().substring(1));
                        InputStream myInputStream = new ByteArrayInputStream(billPDF);
                        InputFile inputFile = new InputFile();
                        inputFile.setMedia(myInputStream, billSummary.getConsumerNo().substring(1) + ".pdf");
                        sendDocument.setReplyMarkup(setInline());
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(chat_id);
                        try {
                            execute(sendDocument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("CURRENT BILL") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        // logger.info(ngbInfoDTO.getBILL_ID());
                        //byte[] billPDF = restTemplateService.getNGBBillPDFbyNgbBillId(ngbInfoDTO.getBILL_ID());
                        ResponseEntity response = restTemplateService.getNGBBillPDFbyBillMonth(billSummary.getConsumerNo().substring(1), billSummary.getBillMonth());
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            sendMessage.setText("No bills present for the consumer");
                            try {
                                execute(sendMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        byte[] billPDF = (byte[]) response.getBody();
                        InputStream myInputStream = new ByteArrayInputStream(billPDF);
                        InputFile inputFile = new InputFile();
                        inputFile.setMedia(myInputStream, billSummary.getConsumerNo().substring(1) + ".pdf");
                        sendDocument.setReplyMarkup(setInline());
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(chat_id);
                        try {
                            execute(sendDocument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("PREVIOUS BILLS") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);

                        ResponseEntity response = restTemplateService.getBillMonths(billSummary.getConsumerNo().substring(1));
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            sendMessage.setText("No bills present for the consumer");
                            try {
                                execute(sendMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;

                        }
                        List<String> billMonths = (List<String>) response.getBody();
                        sendMessage.setReplyMarkup(setConsumerBillMonths(billMonths));
                        sendMessage.setText("Please Select :");
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.matches("CLOSE")) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                    } else if (call_data.substring(0, 9).equals("BILLMONTH") && billSummary != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        // logger.info(ngbInfoDTO.getBILL_ID());
                        //byte[] billPDF = restTemplateService.getNGBBillPDFbyNgbBillId(ngbInfoDTO.getBILL_ID());
                        ResponseEntity response = restTemplateService.getNGBBillPDFbyBillMonth(billSummary.getConsumerNo().substring(1), call_data.substring(9));
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            sendMessage.setText("No bills present for the consumer");
                            try {
                                execute(sendMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        byte[] billPDF = (byte[]) response.getBody();
                        InputStream myInputStream = new ByteArrayInputStream(billPDF);
                        InputFile inputFile = new InputFile();
                        inputFile.setMedia(myInputStream, billSummary.getConsumerNo().substring(1) + ".pdf");
                        sendDocument.setReplyMarkup(setInline());
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(chat_id);
                        try {
                            execute(sendDocument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if (call_data.matches(PATTERN)) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        Message message = new Message();
                        Chat chat = new Chat();
                        chat.setId(Long.valueOf(chat_id));
                        message.setChat(chat);
                        message.setText(call_data);
                        update.setMessage(message);
                        update.setCallbackQuery(null);
                        onUpdateReceived(update);
                    } else {
                        sendMessage.setChatId(chat_id);
                        sendMessage.setText("Hi " + update.getCallbackQuery().getFrom().getFirstName() + " !" + "\nPlease Enter 10 Digit Consumer No.");
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //   List<Map> bills = restTemplateService.getNGBBilling(message_text,"billMonth","DESC",1,12);
          /*  String topBillId = bills.get(0).get("id").toString();
            logger.info(topBillId);
            List<NGBInfoDTO> ngbInfoDTOs = mapper.convertValue(responseEntity.getBody(),  new TypeReference<List<NGBInfoDTO>>() { });
            Double netBill =Double.sum(ngbInfoDTOs.get(0).getMONTH_BILL(),ngbInfoDTOs.get(0).getARRS());
            sendMessage.setText("Hi "+ ngbInfoDTOs.get(0).getCONS_NAME_1() +" your bill for Consumer No :"+message_text+" is Rs. "+netBill);
           // setButtons(sendMessage);*/

         /*   try {
                execute(sendMessage);// Sending our message object to user
                execute(sendDocument);// Sending PDF Bill to user
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

            }
//=====================================Normal Process Ended===============================================================================
//=====================================If No mobile no. or row present in DataBase Started================================================
            else {
                registration(update);
            }
//====================================If No mobile no. or row present in DataBase Ended===================================================
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return Static.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return Static.BOT_TOKEN;
    }


    //=================================Reply KeyBoard Start===============================================================//
    public synchronized ReplyKeyboardMarkup setButtons() {
        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);


        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();
        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        KeyboardButton keyboardButtonFirstRow = new KeyboardButton();
        keyboardButtonFirstRow.setRequestContact(true);
        keyboardButtonFirstRow.setText("Register mobile no.");
        keyboardFirstRow.add(keyboardButtonFirstRow);
        // Second keyboard row
        //KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        //KeyboardButton keyboardButtonSecondRow = new KeyboardButton();
        //keyboardButtonSecondRow.setRequestLocation(true);
        //keyboardButtonSecondRow.setText("Share Location");
        //keyboardSecondRow.add(keyboardButtonSecondRow);


        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        //keyboard.add(keyboardSecondRow);

        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
//=================================Reply KeyBoard End=================================================================//


    //=========================================Inline KeyBoard Start========================================================//
    private InlineKeyboardMarkup setInline() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> consumerDetailButtons = new ArrayList<>();
        // List<InlineKeyboardButton> billDetailButtons = new ArrayList<>();
        //  List<InlineKeyboardButton> readDetailButtons= new ArrayList<>();
        List<InlineKeyboardButton> PDFButtons = new ArrayList<>();
        List<InlineKeyboardButton> paymentButtons = new ArrayList<>();
        List<InlineKeyboardButton> passbookButtons = new ArrayList<>();
        List<InlineKeyboardButton> closeButtons = new ArrayList<>();

        InlineKeyboardButton consumerDetailButton = new InlineKeyboardButton();
        consumerDetailButton.setText("BILL DETAILS");
        consumerDetailButton.setCallbackData("BILL DETAILS");
        //  InlineKeyboardButton billDetailButton = new InlineKeyboardButton();
        //   billDetailButton.setText("BILL DETAILS");
        //  billDetailButton.setCallbackData("BILL DETAILS");
        //   InlineKeyboardButton readDetailButton = new InlineKeyboardButton();
        //     readDetailButton.setText("READ DETAILS");
        //    readDetailButton.setCallbackData("READ DETAILS");
        InlineKeyboardButton PDFButton = new InlineKeyboardButton();
        PDFButton.setText("PDF BILL");
        PDFButton.setCallbackData("PDF BILL");

        //   billDetailButtons.add(billDetailButton);
        //   readDetailButtons.add(readDetailButton);
        InlineKeyboardButton paymentButton = new InlineKeyboardButton();
        paymentButton.setText("Last 10 payments");
        paymentButton.setCallbackData("PAYMENT");

        InlineKeyboardButton passbook = new InlineKeyboardButton();
        passbook.setText("Passbook");
        passbook.setCallbackData("PASSBOOK");

        InlineKeyboardButton close = new InlineKeyboardButton();
        close.setText("CLOSE");
        close.setCallbackData("CLOSE");

        consumerDetailButtons.add(consumerDetailButton);
        paymentButtons.add(paymentButton);
        PDFButtons.add(PDFButton);
        passbookButtons.add(passbook);
        closeButtons.add(close);
        buttons.add(consumerDetailButtons);
        //    buttons.add(billDetailButtons);
        //    buttons.add(readDetailButtons);
        buttons.add(PDFButtons);
        buttons.add(paymentButtons);
        buttons.add(passbookButtons);
        buttons.add(closeButtons);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }


    private InlineKeyboardMarkup setConsumerMobileMapping(List<Map> consumerMobileMappingList) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int i = 0;
        InlineKeyboardButton close = new InlineKeyboardButton();
        List<InlineKeyboardButton> closeButtons = new ArrayList<>();

        for (Map consumerMobileMapping : consumerMobileMappingList) {
            i++;
            List<InlineKeyboardButton> consumerMobileMappingButtons = new ArrayList<>();
            InlineKeyboardButton consumerNo = new InlineKeyboardButton();
            consumerNo.setText(consumerMobileMapping.get("consumerNo").toString() + " : " + consumerMobileMapping.get("consumerName").toString());
            consumerNo.setCallbackData(consumerMobileMapping.get("consumerNo").toString());
            consumerMobileMappingButtons.add(consumerNo);
            buttons.add(consumerMobileMappingButtons);
            if (i == 10) {
                break;
            }
        }

        close.setText("CLOSE");
        close.setCallbackData("CLOSE");
        closeButtons.add(close);
        buttons.add(closeButtons);
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    private InlineKeyboardMarkup setConsumerBillMonths(List<String> billMonths) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        int i = 0;

        for (String billMonth : billMonths) {
            List<InlineKeyboardButton> consumerMobileMappingButtons = new ArrayList<>();
            InlineKeyboardButton consumerNo = new InlineKeyboardButton();
            consumerNo.setText(billMonth);
            consumerNo.setCallbackData("BILLMONTH" + billMonth);
            consumerMobileMappingButtons.add(consumerNo);
            buttons.add(consumerMobileMappingButtons);

        }

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    private InlineKeyboardMarkup setCurrentPreviousBills() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> currentBill = new ArrayList<>();
        List<InlineKeyboardButton> previousBills = new ArrayList<>();

        InlineKeyboardButton current = new InlineKeyboardButton();
        current.setText("CURRENT BILL");
        current.setCallbackData("CURRENT BILL");
        currentBill.add(current);

        InlineKeyboardButton previous = new InlineKeyboardButton();
        previous.setText("PREVIOUS BILLS");
        previous.setCallbackData("PREVIOUS BILLS");
        previousBills.add(previous);

        buttons.add(currentBill);
        buttons.add(previousBills);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

//=========================================Inline KeyBoard End==========================================================//

    //=========================================Basic Registration Start======================================================//
    public void registration(Update update) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            String message_text = update.getMessage().getText();
            ResponseEntity responseEntity = null;
            boolean hasContact = update.getMessage().hasContact();

            if (!hasContact) {
                sendMessage.setText("Register your mobile no. with chatbot");
                sendMessage.setReplyMarkup(setButtons());
                execute(sendMessage);
                return;
            }


            if (hasContact) {
                boolean verifyMobile = update.getMessage().getContact().getUserId().toString().equals(update.getMessage().getFrom().getId().toString());
                if (verifyMobile) {
                    ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
                    replyKeyboardRemove.setRemoveKeyboard(true);
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("Your mobile no. registered successfully");
                    sendMessage.setReplyMarkup(replyKeyboardRemove);
                    ResponseEntity responseEntity1=null;
                    try{
                    responseEntity1=restTemplateService.putMobileChatid(update.getMessage().getContact().getPhoneNumber().substring(2),update.getMessage().getChatId().toString());}
                    catch (Exception e)
                    {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                    if(responseEntity1==null)
                        return;
                    if(responseEntity1.getStatusCode()!= HttpStatus.OK)
                        return;
                    telegramMobileRepository.save(new TelegramMobileEntity(update.getMessage().getFrom().getId().toString(), update.getMessage().getContact().getPhoneNumber().substring(2), update));
                    execute(sendMessage);
                    return;
                } else {
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("Please enter your own mobile no. attached with telegram only ");
                    execute(sendMessage);
                    return;
                }
            }

//=================================================================Consumer No Save Logic Started======================================================
          /*  if (telegramMobileConsumerMappingRepository.findByChatID(update.getMessage().getChatId().toString()) == null) {
                sendMessage.setText("Please Register Your Mobile No. & 10 Digit Consumer No with us to use the bot services");
                execute(sendMessage);
                sendMessage.setText("Please Type Mobile No.");
                sendMessage.setReplyMarkup(setButtons());
                execute(sendMessage);
                String message = update.getMessage().getText();
                if (!message.matches(PATTERN)) {
                    sendMessage.setText("Please Type Correct Mobile No.");
                    execute(sendMessage);
                } else {
                    TelegramMobileConsumerMappingEntity telegramMobileConsumerMappingEntity1 = new TelegramMobileConsumerMappingEntity(update.getMessage().getChatId().toString(), update.getMessage().getText());
                    telegramMobileConsumerMappingRepository.save(telegramMobileConsumerMappingEntity1);
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("Ok , Mobile No. Registered");
                    execute(sendMessage);
                    sendMessage.setText("Now Type 10 Digit Consumer No");
                    execute(sendMessage);
                    return;
                }
            }

            else {
                if (!update.getMessage().getText().matches(PATTERN)) {
                    sendMessage.setText("Please Type 10 Digit Consumer No.");
                    execute(sendMessage);
                } else if (responseEntity == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                    sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " " + " ! \nThis is Wrong Consumer No");
                    execute(sendMessage);
                } else {
                    TelegramMobileConsumerMappingEntity telegramMobileConsumerMappingEntity1 = telegramMobileConsumerMappingRepository.findByChatID(update.getMessage().getChatId().toString());
                    List<String> consumerNo = new ArrayList<>();
                    consumerNo.add(update.getMessage().getText());
                    telegramMobileConsumerMappingEntity1.setConsumerNo(consumerNo);
                    telegramMobileConsumerMappingRepository.save(telegramMobileConsumerMappingEntity1);
                    sendMessage.setText("Ok , Consumer No. Registered");
                    execute(sendMessage);
                }
                return;
            } */
//========================================================================Consumer No. Save Logic Ended=============================================
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//=========================================Basic Registration End========================================================//
}





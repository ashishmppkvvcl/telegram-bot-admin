package com.mppkvvcl.telegrambotadmin.botcode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mppkvvcl.telegrambotadmin.dto.NGBInfoDTO;
import com.mppkvvcl.telegrambotadmin.entity.TelegramEntity;
import com.mppkvvcl.telegrambotadmin.entity.TelegramMobileEntity;
import com.mppkvvcl.telegrambotadmin.repository.TelegramMobileRepository;
import com.mppkvvcl.telegrambotadmin.repository.TelegramRepository;
import com.mppkvvcl.telegrambotadmin.service.RestTemplateService;
import com.mppkvvcl.telegrambotadmin.utility.LoggerUtil;
import com.mppkvvcl.telegrambotadmin.utility.Static;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
            List<NGBInfoDTO> ngbInfoDTOs = null;
            NGBInfoDTO ngbInfoDTO = null;
            String chatId = null;


            if(update.hasMessage())
            {
                chatId=update.getMessage().getChatId().toString();
            }

            if(update.hasCallbackQuery())
            {
                chatId=update.getCallbackQuery().getMessage().getChatId().toString();
            }

            TelegramMobileEntity telegramMobileEntity = telegramMobileRepository.findByChatID(chatId);

//=====================================================Normal Process Started ==========================================================
            if (telegramMobileEntity != null) {
                //==========================================Simple Message Start=====================================================//
// We check if the update has a message and the message has text
                if (update.hasMessage() && update.getMessage().hasText()) {
                    String message_text = null;
                    ResponseEntity responseEntity = null;
                    message_text = update.getMessage().getText();

                    if (message_text.equals("/start")) {
                        sendMessage.setChatId(update.getMessage().getChatId().toString());
                        sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " !" + "\nPlease Enter 10 Digit Consumer No.");
                        ngbInfoDTO = null;
                        message_text = null;
                        try {
                            execute(sendMessage); // Sending our message object to user
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    String chat_id = update.getMessage().getChatId().toString();
                    sendMessage.setChatId(chat_id);
                    if (message_text.matches(PATTERN)) {
                        responseEntity = restTemplateService.getNGBInfo(message_text);
                    } else {
                        sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " !" + "\nPlease Enter 10 Digit Consumer No.");
                        ngbInfoDTO = null;
                        message_text = null;
                        try {
                            execute(sendMessage); // Sending our message object to user
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }


                    if (responseEntity == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                        String lastName = update.getMessage().getFrom().getLastName();
                        if (lastName == null) {
                            lastName = "";
                        }
                        sendMessage.setText("Hi " + update.getMessage().getFrom().getFirstName() + " " + lastName + " ! \nThis is Wrong Consumer No");
                        ngbInfoDTO = null;
                        message_text = null;
                        try {
                            execute(sendMessage); // Sending our message object to user
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    ngbInfoDTOs = mapper.convertValue(responseEntity.getBody(), new TypeReference<List<NGBInfoDTO>>() {
                    });
                    ngbInfoDTO = ngbInfoDTOs.get(0);


                    TelegramEntity telegramEntity = new TelegramEntity(chat_id, message_text, ngbInfoDTO,update);
                    TelegramEntity telegramEntity1 = telegramRepository.findTopByChatIDOrderByIdDesc(chatId);
                    if(telegramEntity1!=null)
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
                if (update.hasCallbackQuery() ) {
                    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                    EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
                    TelegramMobileEntity telegramMobileEntity1 = telegramMobileRepository.findByChatID(update.getCallbackQuery().getMessage().getChatId().toString());
                    Double netBill = null;
                    String call_data = update.getCallbackQuery().getData();
                    String chat_id = update.getCallbackQuery().getMessage().getChatId().toString();
                    sendMessage.setChatId(chat_id);
                    TelegramEntity telegramEntity = telegramRepository.findTopByChatIDOrderByIdDesc(chat_id);
                    if (telegramEntity != null) {
                        ngbInfoDTO = telegramEntity.getNgbInfoDTO();
                    }
                    if (telegramEntity == null)
                        ngbInfoDTO = null;


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

                    if (call_data.equals("BILL DETAILS") && ngbInfoDTO != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        netBill=Double.sum(ngbInfoDTO.getMONTH_BILL(),ngbInfoDTO.getARRS());
                        sendMessage.setText(
                                        "Your Details are :\r\n" +
                                        "Consumer No. :" + ngbInfoDTO.getCONS_NO_1() + "\n" +
                                        "Consumer Name : " + ngbInfoDTO.getCONS_NAME_1() + "\n" +
                                        "Consumer Address : " + ngbInfoDTO.getADDR_1() + " " + ngbInfoDTO.getADDR_2() + " " + ngbInfoDTO.getADDR_3() + "\n" +
                                        "Tariff : " + ngbInfoDTO.getTARIFF() + "\n" +
                                        "Bill Month : " + ngbInfoDTO.getBILL_MONTH_1() + "\n" +
                                        "Read Date :" + ngbInfoDTO.getRDG_DATE() + "\n" +
                                        "Current Read :" + ngbInfoDTO.getRDG_CURR() + "\n" +
                                        "Bill Date :" + ngbInfoDTO.getBILL_DATE() + "\n" +
                                        "Current Bill :" + ngbInfoDTO.getMONTH_BILL() + "\n" +
                                        "Net Bill :" + netBill + "\n" +
                                        "Due By :" + ngbInfoDTO.getCSH_DATE_2()
                        );
                        try {
                            execute(sendMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (call_data.equals("PDF BILL") && ngbInfoDTO != null) {
                        editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        editMessageReplyMarkup.setReplyMarkup(null);
                        editMessageReplyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                        execute(editMessageReplyMarkup);
                        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                        answerCallbackQuery.setText("");
                        answerCallbackQuery.setShowAlert(true);
                        execute(answerCallbackQuery);
                        logger.info(ngbInfoDTO.getBILL_ID());
                        byte[] billPDF = restTemplateService.getNGBBillPDFbyNgbBillId(ngbInfoDTO.getBILL_ID());
                        InputStream myInputStream = new ByteArrayInputStream(billPDF);
                        InputFile inputFile = new InputFile();
                        inputFile.setMedia(myInputStream, ngbInfoDTO.getCONS_NO_1().substring(1) + ".pdf");
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(chat_id);
                        try {
                            execute(sendDocument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        return Static.prodBotUsername;
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return Static.prodBotToken;
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


    //=========================================Inline KeyBoard Satrt========================================================//
    private InlineKeyboardMarkup setInline() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> consumerDetailButtons = new ArrayList<>();
        // List<InlineKeyboardButton> billDetailButtons = new ArrayList<>();
        //  List<InlineKeyboardButton> readDetailButtons= new ArrayList<>();
        List<InlineKeyboardButton> PDFButtons = new ArrayList<>();
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
        consumerDetailButtons.add(consumerDetailButton);
        //   billDetailButtons.add(billDetailButton);
        //   readDetailButtons.add(readDetailButton);
        PDFButtons.add(PDFButton);
        buttons.add(consumerDetailButtons);
        //    buttons.add(billDetailButtons);
        //    buttons.add(readDetailButtons);
        buttons.add(PDFButtons);
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
            sendMessage.setText("Hi ! "+update.getMessage().getFrom().getFirstName()+"\n Go to https://cmi.mpwin.co.in for registration  .Your chat id is : "+update.getMessage().getFrom().getId());
            logger.info(execute(sendMessage).getText());
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
         catch (Exception e) {
            e.printStackTrace();
        }

    }
//=========================================Basic Registration End========================================================//
}




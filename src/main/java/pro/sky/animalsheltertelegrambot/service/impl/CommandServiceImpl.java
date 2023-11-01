package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;
import pro.sky.animalsheltertelegrambot.service.CommandService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandServiceImpl implements CommandService {

    private final TelegramBot telegramBot;
    private String text = " ";
    public CommandServiceImpl(TelegramBot telegramBot, ShelterRepository shelterRepository) {
        this.telegramBot = telegramBot;
        this.shelterRepository = shelterRepository;
    }
    public final ShelterRepository shelterRepository;

    @Override
    public SendMessage executeStartCommandIfUserExists(Long chatId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("DOGS  \uD83D\uDC36").callbackData("DOGS"),
                new InlineKeyboardButton("CATS \uD83D\uDC31").callbackData("CATS"));
        String testStr = "Выберите приют: \uD83D\uDC3E";
        SendMessage sendMessage = new SendMessage(chatId, testStr).replyMarkup(inlineKeyboard);
        return sendMessage;
    }

    @Override
    public void receivedCallbackMessage(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.data();
        Long chatId = callbackQuery.from().id();
        if(call_data.equals("CATS")){
            SendMessage sendMessage = displayMainMenu(chatId, " Приют для кошек");
            telegramBot.execute(sendMessage);
        }
        if(call_data.equals("DOGS")){
            SendMessage sendMessage = displayMainMenu(chatId, "Приют для собак");
            telegramBot.execute(sendMessage);
        }
        if(call_data.equals("INFO")){
            telegramBot.execute(displayMenuShelterInfo(chatId));
        }
        if(call_data.equals("INFO_DOG")){
            telegramBot.execute(displayAboutShelter(chatId));
//            String filePath = "file:///C:/Users/user/Desktop/IT/dog_shelter_info_.pdf";
//            File file = downloadFile(filePath, outputFile);
        }

    }
    public SendMessage displayMainMenu(Long chatId, String text){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Информация о приюте").callbackData("INFO");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Взять животное").callbackData("ADOPT");
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton("Прислать отчет").callbackData("REPORT");
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton("Позвать волонтера").callbackData("VOLUNTEER");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);

        inlineKeyboardMarkup.addRow(inlineKeyboardButton1, inlineKeyboardButton2);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton3, inlineKeyboardButton4);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    @Override
    public SendMessage displayMenuShelterInfo(Long chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("О приюте").callbackData("INFO_DOG");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Расписание, адрес\nи схема проезда").callbackData("SCHEDULE");
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton("Контакты охраны").callbackData("SECURITY");
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton("Рекомендации \nпо технике безопасности\nна территории приюта").callbackData("RECOMMENDATION");
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton("Оставить контактные\nданнные для связи").callbackData("CONTACTS");
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton("Позвать волонтера").callbackData("VOLONTEER_DOG_SHELTER");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);
        keyboardButtonsRow3.add(inlineKeyboardButton5);
        keyboardButtonsRow3.add(inlineKeyboardButton6);

        inlineKeyboardMarkup.addRow(inlineKeyboardButton1, inlineKeyboardButton2);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton3, inlineKeyboardButton4);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton5, inlineKeyboardButton6);

        SendMessage sendMessage = new SendMessage(chatId, "Подробная информация о приюте").replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    @Override
    public SendMessage displayAboutShelter(Long chatId){
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findByShelterType());
        return sendMessage;
    }
}

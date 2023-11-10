package pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.RegularUserStartEvent;

/*
Класс CommandServiceImpl отвечает за обработку команд, исходящих от пользователя в Telegram боте, и отправку соответствующих сообщений через MessageSendingService.
 */
public interface CommandService {

//    public void onUserCreated(StartCommandEvent event);

    public void handleStartCommandEvent(RegularUserStartEvent event);
    public void receivedCallbackMessage(CallbackQuery callbackQuery);

    public SendMessage executeStartCommandIfUserExists(Long chatId);

    public SendMessage firstMenuDog(Long chatId, String text);

    public InlineKeyboardMarkup createShelterInfoMenu(Button... buttons);

    public SendMessage firsMenuCat(Long chatId, String text);

    public SendMessage runMenuShelterInfo(Long chatId);

    public void runMenuForAdopter(Long chatId, String userName);

}

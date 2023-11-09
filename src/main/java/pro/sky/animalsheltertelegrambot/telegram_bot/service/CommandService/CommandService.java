package pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;

public interface CommandService {

    public void receivedCallbackMessage(CallbackQuery callbackQuery);

    public SendMessage executeStartCommandIfUserExists(Long chatId);

    public SendMessage firstMenuDog(Long chatId, String text);

    public SendMessage firsMenuCat(Long chatId, String text);

    public SendMessage runMenuShelterInfo(Long chatId);

    public void runMenuForAdopter(Long chatId);

    public void sendDocument(String path, Long chatId);

}

package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {
    public void processTextMessage(Long chatId, String text);

    public void receivedCallbackMessage(CallbackQuery callbackQuery);

    public SendMessage executeStartCommandIfUserExists(Long chatId);

    public SendMessage runMainMenu(Long chatId, String text);

    public SendMessage runMainMenuForCat(Long chatId, String text);

    public SendMessage runMenuShelterInfo(Long chatId);

    public SendMessage runMenuShelterInfoForCat(Long chatId);

    public void runMenuForAdopter(Long chatId);

    public void processStartCommand(Long chatId, String s);

    public void sendDocument(String path, Long chatId);
}

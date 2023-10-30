package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {
    SendMessage executeStartCommandIfUserExists(Long chatId);

    void receivedCallbackMessage(CallbackQuery callbackQuery);
}

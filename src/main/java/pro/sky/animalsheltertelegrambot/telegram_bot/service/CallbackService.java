package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.CallbackQuery;

public interface CallbackService {

    public void handleCallback(CallbackQuery callbackQuery);

    public void processCallback(CallbackQuery callbackQuery);
}

package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.CallbackQuery;

public interface InteractionService {

    public void processCallback(CallbackQuery callbackQuery);
}

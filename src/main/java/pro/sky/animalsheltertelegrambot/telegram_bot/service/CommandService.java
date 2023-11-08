package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {
    SendMessage executeStartCommandIfUserExists(Long chatId);

    void receivedCallbackMessage(CallbackQuery callbackQuery);


    SendMessage runMainMenu(Long chatId, String text);

    SendMessage runMainMenuForCat(Long chatId, String text);

    SendMessage runMenuShelterInfo(Long chatId);

    SendMessage runMenuShelterInfoForCat(Long chatId);

    SendMessage displayDogShelterContacts(Long chatId);

    SendMessage displayCatShelterContacts(Long chatId);

    SendMessage displayDogShelterSecurityContacts(Long chatId);

//    SendMessage displayCatShelterSecurityContacts(Long chatId);

    SendMessage displayDogShelterWorkingHours(Long chatId);

//    SendMessage displayCatShelterWorkingHours(Long chatId);

    SendMessage displayReportInfo(Long chatId);

    /**
     * Создает меню выбора действия для пользователя-усыновителя. <p>
     * Возможные действия: <p>
     * - отправить отчет; <p>
     * - выбрать еще одного питомца; <p>
     * - помощь волонтера.
     * @param chatId ID чата с текущим пользователем
     */
    void runMenuForAdopter(Long chatId);

    void saveReport(Message message);
}

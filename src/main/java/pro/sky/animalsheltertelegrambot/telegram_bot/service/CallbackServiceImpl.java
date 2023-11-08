package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.APPLICATION;

/**
 * Сервис для обработки колбэк-запросов от inline-кнопок телеграм-бота.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final TelegramBot telegramBot;
    private final AdoptionService adoptionService;
    private final PetService petService;

    //метод для обработки callbackQuery
    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.data();
        Long chatId = callbackQuery.from().id();
        SendMessage sendMessage;
        String path = "";

        switch (call_data) {
            case "CATS":
                sendMessage = runMainMenuForCat(chatId, " Приют для кошек");
                telegramBot.execute(sendMessage);
                break;
            case "DOGS":
                sendMessage = runMainMenu(chatId, "Приют для собак");
                telegramBot.execute(sendMessage);
                break;
            case "ABOUT_SHELTER":
                telegramBot.execute(runMenuShelterInfo(chatId));
                break;
            case "ABOUT_SHELTER_CAT":
                telegramBot.execute(runMenuShelterInfoForCat(chatId));
                break;
            case "SHELTER_INFO":
                telegramBot.execute(displayDogShelterContacts(chatId));
                path = "src/main/resources/files/dog_shelter_info_.pdf";
                sendDocument(path,chatId);
                break;
//            case "CAT_SHELTER_INFO":
//                telegramBot.execute(displayCatShelterContacts(chatId));
//                break;
            case "SECURITY_CONTACTS":
                telegramBot.execute(displayDogShelterSecurityContacts(chatId));
                path = "src/main/resources/files/dog_shelter_security_contacts.pdf";
                sendDocument(path,chatId);
                break;
//            case "SECURITY_CONTACTS_CAT":
//                telegramBot.execute(displayCatShelterSecurityContacts(chatId));
//                break;
            case "SCHEDULE":
                telegramBot.execute(displayDogShelterWorkingHours(chatId));
                path = "src/main/resources/files/dog_shelter_schedule_address.pdf";
                sendDocument(path,chatId);
                break;
            case "SAFETY_RECOMMENDATION":
                path = "src/main/resources/files/dog_safety_recommendation.pdf";
                sendDocument(path,chatId);
                break;

            case "APPLICATION":
                startAdoptionProcess(chatId);
                break;

//            case "SCHEDULE_CAT":
//                telegramBot.execute(displayCatShelterWorkingHours(chatId));
//                break;

            case "REPORT":
                telegramBot.execute(displayReportInfo(chatId));
                break;
        }
    }

    /**
     * Обрабатывает колбэк-запросы, полученные от пользователей через inline-кнопки.
     *
     * @param callbackQuery объект колбэк-запроса, полученный от Telegram API
     */
    public void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        log.info("Обработка колбэк-запроса с данными: {} для чата: {}", callbackData, chatId);

        if (callbackData.equals(APPLICATION.toString())) {
            log.info("Запрос контактной информации для пользователя с чат-идентификатором: {}", chatId);
            adoptionService.requestContactInfo(chatId, telegramBot);
        } else if (callbackData.startsWith("ANIMAL_")) {
            Long animalId = Long.parseLong(callbackData.split("_")[1]);
            log.info("Обработка информации о животном с идентификатором: {} для пользователя с чат-идентификатором: {}", animalId, chatId);
            petService.sendAnimalDetails(chatId, animalId);
        } else {
            log.warn("Получены неизвестные данные колбэка: {}", callbackData);
            // Здесь могут быть дополнительные обработчики для других типов колбэков
        }
    }
}

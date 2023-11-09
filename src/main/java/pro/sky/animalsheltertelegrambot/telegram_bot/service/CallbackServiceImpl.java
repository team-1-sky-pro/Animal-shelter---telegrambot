package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.APPLICATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final AdoptionService adoptionService;
    private final CommandService commandService;
    private final MessageService messageService;
    private final ShelterService shelterService;
    private final ReportService reportService;

    /**
     * Обрабатывает колбэк-запросы, полученные от пользователей через inline-кнопки.
     *
     * @param callbackQuery объект колбэк-запроса, полученный от Telegram API
     */
    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        log.info("Обработка колбэк-запроса с данными: {} для чата: {}", callbackData, chatId);

        switch (callbackData) {
            case "CATS":
                commandService.runMainMenuForCat(chatId, "Приют для кошек");
                break;
            case "DOGS":
                commandService.runMainMenu(chatId, "Приют для собак");
                break;
            case "ABOUT_SHELTER":
                commandService.runMenuShelterInfo(chatId);
                break;
            case "ABOUT_SHELTER_CAT":
                commandService.runMenuShelterInfoForCat(chatId);
                break;
            case "SHELTER_INFO":
                shelterService.displayDogShelterContacts(chatId);
                messageService.sendDocument("src/main/resources/files/dog_shelter_info_.pdf", chatId);
                break;
            // Случаи "CAT_SHELTER_INFO" и "SECURITY_CONTACTS_CAT" закомментированы, если они не нужны, удалите их.
            case "SECURITY_CONTACTS":
                shelterService.displayDogShelterSecurityContacts(chatId);
                messageService.sendDocument("src/main/resources/files/dog_shelter_security_contacts.pdf", chatId);
                break;
            case "SCHEDULE":
                shelterService.displayDogShelterWorkingHours(chatId);
                messageService.sendDocument("src/main/resources/files/dog_shelter_schedule_address.pdf", chatId);
                break;
            case "SAFETY_RECOMMENDATION":
                messageService.sendDocument("src/main/resources/files/dog_safety_recommendation.pdf", chatId);
                break;
            case "APPLICATION":
                adoptionService.startAdoptionProcess(chatId);
                break;
            case "REPORT":
                reportService.displayReportInfo(chatId);
                break;
            default:
                log.warn("Получены неизвестные данные колбэка: {}", callbackData);
                // Здесь могут быть дополнительные обработчики для других типов колбэков
                break;
        }
    }
}


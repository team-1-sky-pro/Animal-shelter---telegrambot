package pro.sky.animalsheltertelegrambot.telegram_bot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.ShelterService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService.CommandService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService.MessageService;


@Slf4j
@RequiredArgsConstructor
@Component
public class CallbackListener {

    private final MessageSendingService messageSendingService;
    private final ShelterService shelterService;
    private final MessageService messageService;
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final CommandService commandService;
    private final TelegramBot telegramBot;

    @EventListener
    public void onApplicationEvent(CallbackEvent event) {
        CallbackQuery callbackQuery = event.getCallbackQuery();
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        log.info("Обработка колбэк-запроса с данными: {} для чата: {}", callbackData, chatId);

        // Проверяем, начинаются ли данные колбэка с "ANIMAL_"
        if (adoptionService.isAdoptionCallback(callbackData)) {
            // Если да, то перенаправляем обработку в метод handleAdoptionCallback класса AdoptionService
            adoptionService.handleAdoptionCallback(callbackQuery, telegramBot);
        } else {
            // Все остальные колбэки обрабатываем как раньше
            switch (callbackData) {
                case "CATS":
                    log.info("Обработка выбора приюта для кошек для chatId: {}", chatId);
                    SendMessage messageForCats = commandService.firsMenuCat(chatId, "Приют для кошечек:");
                    messageSendingService.sendMessage(messageForCats);
                    break;
                case "DOGS":
                    log.info("Обработка выбора приюта для собак для chatId: {}", chatId);
                    SendMessage messageForDogs = commandService.firstMenuDog(chatId, "Приют для собачек:");
                    messageSendingService.sendMessage(messageForDogs);
                    break;
                case "ABOUT_SHELTER":
                    log.info("Обработка выбора информация о приюте для собачек для chatId: {}", chatId);
                    SendMessage shelterInfo = commandService.runMenuShelterInfo(chatId);
                    messageSendingService.sendMessage(shelterInfo);
                    break;
                case "ABOUT_SHELTER_CAT":
                    log.info("Обработка выбора информация о приюте для котиков для chatId: {}", chatId);
                    SendMessage shelterInfoCat = commandService.runMenuShelterInfo(chatId);
                    messageSendingService.sendMessage(shelterInfoCat);
                    break;
                case "SHELTER_INFO":
                    log.info("Обработка информации о приюте для chatId: {}", chatId);
                    SendMessage shelterInfoPro = shelterService.displayDogShelterContacts(chatId);
                    messageSendingService.sendMessage(shelterInfoPro);
                    commandService.sendDocument("src/main/resources/files/dog_shelter_info_.pdf", chatId);
                    break;
                case "SECURITY_CONTACTS":
                    SendMessage securityContacts = shelterService.displayDogShelterSecurityContacts(chatId);
                    messageSendingService.sendMessage(securityContacts);
                    messageService.sendDocument("src/main/resources/files/dog_shelter_security_contacts.pdf", chatId);
                    break;
                case "SCHEDULE":
                    SendMessage schedule = shelterService.displayDogShelterWorkingHours(chatId);
                    messageSendingService.sendMessage(schedule);
                    messageService.sendDocument("src/main/resources/files/dog_shelter_schedule_address.pdf", chatId);
                    break;
                case "SAFETY_RECOMMENDATION":
                    String safetyRecommendationText = "Подробности по безопасности на территории приюта.";
                    messageSendingService.sendMessage(chatId, safetyRecommendationText);
                    messageService.sendDocument("src/main/resources/files/dog_safety_recommendation.pdf", chatId);
                    break;
                case "APPLICATION_CAT":
                    log.info("Обработка заявки на усыновление кошки для chatId: {}", chatId);
                    // Вызов метода, который начнет процесс усыновления кошки
                    adoptionService.startAdoptionProcess(chatId, 1L); // 1 для кошек
                    break;
                case "APPLICATION_DOG":
                    log.info("Обработка заявки на усыновление собаки для chatId: {}", chatId);
                    // Вызов метода, который начнет процесс усыновления собаки
                    adoptionService.startAdoptionProcess(chatId, 2L);
                    break;
                case "REPORT":
                    reportService.displayReportInfo(chatId);
                    break;
                default:
                    log.warn("Получены неизвестные данные колбэка: {}", callbackData);
                    break;
            }
        }
    }
}

package pro.sky.animalsheltertelegrambot.telegram_bot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.ShelterService;
import pro.sky.animalsheltertelegrambot.service.UserService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.ReportStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService.CommandService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService.MessageService;

/**
 * Компонент, слушающий колбэк-события от Telegram бота.
 * Распределяет колбэк-запросы по соответствующим обработчикам в зависимости от содержащихся в них данных.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CallbackListener {

    private final MessageSendingService messageSendingService;
    private final ShelterService shelterService;
    private final UserService userService;
    private final MessageService messageService;
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final CommandService commandService;
    private final TelegramBot telegramBot;

    /**
     * Обработчик колбэк-событий, получаемых от inline-кнопок в сообщениях Telegram бота.
     *
     * @param event Событие, содержащее колбэк-запрос от пользователя.
     */
    @EventListener
    public void onApplicationEvent(CallbackEvent event) {
        CallbackQuery callbackQuery = event.getCallbackQuery();
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        log.info("Обработка колбэк-запроса: '{}' для chatId: {}", callbackData, chatId);

        if (adoptionService.isAdoptionCallback(callbackData)) {
            adoptionService.handleAdoptionCallback(callbackQuery, telegramBot);
        } else {
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
                    messageService.sendDocument("src/main/resources/files/dog_shelter_info_.pdf", chatId);
                    break;
                case "SECURITY_CONTACTS":
                    log.info("Обработка информации о securityContacts для chatId: {}", chatId);
                    SendMessage securityContacts = shelterService.displayDogShelterSecurityContacts(chatId);
                    messageSendingService.sendMessage(securityContacts);
                    messageService.sendDocument("src/main/resources/files/dog_shelter_security_contacts.pdf", chatId);
                    break;
                case "SCHEDULE":
                    log.info("Обработка информации о schedule для chatId: {}", chatId);
                    SendMessage schedule = shelterService.displayDogShelterWorkingHours(chatId);
                    messageSendingService.sendMessage(schedule);
                    messageService.sendDocument("src/main/resources/files/dog_shelter_schedule_address.pdf", chatId);
                    break;
                case "SAFETY_RECOMMENDATION":
                    log.info("Обработка информации о safetyRecommendationText для chatId: {}", chatId);
                    String safetyRecommendationText = "Подробности по безопасности на территории приюта.";
                    messageSendingService.sendMessage(chatId, safetyRecommendationText);
                    messageService.sendDocument("src/main/resources/files/dog_safety_recommendation.pdf", chatId);
                    break;
                case "APPLICATION_CAT":
                    log.info("Обработка заявки на усыновление кошки для chatId: {}", chatId);
                    adoptionService.startAdoptionProcess(chatId, 1L); // 1 для кошек
                    break;
                case "APPLICATION_DOG":
                    log.info("Обработка заявки на усыновление собаки для chatId: {}", chatId);
                    adoptionService.startAdoptionProcess(chatId, 2L);
                    break;
                case "REPORT":
                    log.info("Обработка отправки отчета для chatId: {}", chatId);
                    reportService.displayReportInfo(chatId);
                    break;
                case "VOLUNTEER":
                    log.info("Отправлено сообщение волонтеру от chatId: {}", chatId);
                    userService.sendVolunteerChat(chatId);
                    break;
                case "HOW_TO_TAKE_PET":
                    log.info("Обработка информации об how_to_take_pet для chatId: {}", chatId);
                    String message = "Подробную информацию, как взять животное, вы найдете в файле.";
                    messageSendingService.sendMessage(chatId, message);
                    messageService.sendDocument("src/main/resources/files/how_to_take_pet.pdf", chatId);
                    break;
                case "ANOTHER_PET":
                    commandService.executeStartCommandIfUserExists(chatId);
                    SendMessage sendMessage = commandService.executeStartCommandIfUserExists(chatId);
                    messageSendingService.sendMessage(sendMessage);
                default:
                    log.warn("Получены неизвестные данные колбэка: {}", callbackData);
                    break;
            }
        }
    }
}

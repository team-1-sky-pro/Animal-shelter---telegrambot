package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.ShelterService;
import pro.sky.animalsheltertelegrambot.service.UserService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.StartCommandEvent;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final TelegramBot telegramBot;
    private final CallbackService callbackService;
    private final ShelterService shelterService;
    private final MessageService messageService;
    private final AdoptionService adoptionService;
    private final ReportService reportService;
    private final MessageSendingService messageSendingService;


    @EventListener
    public void onUserCreated(StartCommandEvent event) {
        // Реакция на событие, например, показать меню приюта
        runMainMenu(event.getChatId(), "Выберите приют:");
    }


    @Override
    @EventListener
    public void onApplicationEvent(CallbackEvent event) {
        CallbackQuery callbackQuery = event.getCallbackQuery();
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        log.info("Обработка колбэк-запроса с данными: {} для чата: {}", callbackData, chatId);

        switch (callbackData) {
            case "CATS":
                log.info("Обработка выбора приюта для кошек для chatId: {}", chatId);
                SendMessage messageForCats = runMainMenuForCat(chatId, "Приют для кошек:");
                messageSendingService.sendMessage(messageForCats);
                break;
            case "DOGS":
                log.info("Обработка выбора приюта для собак для chatId: {}", chatId);
                SendMessage messageForDogs = runMainMenu(chatId, "Приют для собак");
                messageSendingService.sendMessage(messageForDogs);
                break;
            case "ABOUT_SHELTER":
                runMenuShelterInfo(chatId);
                break;
            case "ABOUT_SHELTER_CAT":
                runMenuShelterInfoForCat(chatId);
                break;
            case "SHELTER_INFO":
                shelterService.displayDogShelterContacts(chatId);
                sendDocument("src/main/resources/files/dog_shelter_info_.pdf", chatId);
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


    @Override
    public void receivedCallbackMessage(CallbackQuery callbackQuery) {
        callbackService.processCallback(callbackQuery);
    }


    @Override
    public SendMessage executeStartCommandIfUserExists(Long chatId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("DOGS  \uD83D\uDC36").callbackData("DOGS"),
                new InlineKeyboardButton("CATS \uD83D\uDC31").callbackData("CATS"));
        String testStr = "Выберите приют: \uD83D\uDC3E";
        SendMessage sendMessage = new SendMessage(chatId, testStr).replyMarkup(inlineKeyboard);
        log.info("Отправили сообщение о выборе приюта chatId: {}" , chatId);
        return sendMessage;
    }

    private InlineKeyboardMarkup createShelterInfoMenu(String... buttonLabels) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (String label : buttonLabels) {
            InlineKeyboardButton button = new InlineKeyboardButton(label);
            button.callbackData(label);
            inlineKeyboardMarkup.addRow(button);
        }
        return inlineKeyboardMarkup;
    }


    @EventListener
    public void handleStartCommandEvent(StartCommandEvent event) {
        log.info("Показ основного меню для chatId: {}", event.getChatId());
        SendMessage sendMessage = executeStartCommandIfUserExists(event.getChatId());
        // Отправка основного меню пользователю через MessageSendingService
        messageSendingService.sendMessage(sendMessage);
    }

    @Override
    public SendMessage runMainMenu(Long chatId, String text) {
        log.info("Вызывается метод runMainMenu");
        //кнопки для основного меню
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER.getText());
        aboutShelterButton.callbackData(ABOUT_SHELTER.toString());

            InlineKeyboardButton reportButton = new InlineKeyboardButton(REPORT.getText());
        reportButton.callbackData(REPORT.toString());

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton);
        inlineKeyboardMarkup.addRow(reportButton, volunteerButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    // Основное меню после выбора приюта (dog/cat)
    @Override
    public SendMessage runMainMenuForCat(Long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER.getText());
        aboutShelterButton.callbackData(ABOUT_SHELTER.toString());

        InlineKeyboardButton adoptAnimalButton = new InlineKeyboardButton(ADOPT_ANIMAL.getText());
        adoptAnimalButton.callbackData(ADOPT_ANIMAL.toString());

        InlineKeyboardButton reportButton = new InlineKeyboardButton(REPORT.getText());
        reportButton.callbackData(REPORT.toString());

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton, adoptAnimalButton);
        inlineKeyboardMarkup.addRow(reportButton, volunteerButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage runMenuShelterInfo(Long chatId) {
        //кнопки для подробного меню
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton dogShelterInfo = new InlineKeyboardButton(SHELTER_INFO.getText());
        dogShelterInfo.callbackData(SHELTER_INFO.toString());

        InlineKeyboardButton scheduleButton = new InlineKeyboardButton(SCHEDULE.getText());
        scheduleButton.callbackData(SCHEDULE.toString());

        InlineKeyboardButton securityContacts = new InlineKeyboardButton(SECURITY_CONTACTS.getText());
        securityContacts.callbackData(SECURITY_CONTACTS.toString());

        InlineKeyboardButton safetyRecommendationButton = new InlineKeyboardButton(SAFETY_RECOMMENDATION.getText());
        safetyRecommendationButton.callbackData(SAFETY_RECOMMENDATION.toString());

        InlineKeyboardButton applicationButton = new InlineKeyboardButton(APPLICATION.getText());
        applicationButton.callbackData(APPLICATION.toString());

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(dogShelterInfo, scheduleButton);
        inlineKeyboardMarkup.addRow(securityContacts, safetyRecommendationButton);
        inlineKeyboardMarkup.addRow(applicationButton, volunteerButton);

        SendMessage sendMessage = new SendMessage(chatId, "Подробная информация о приюте").replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }


    // Меню после нажатия кнопки "О приюте" -> попадаем в меню подробной информации
    @Override
    public SendMessage runMenuShelterInfoForCat(Long chatId) {
        // Подробная информация о приюте кошек
        InlineKeyboardMarkup inlineKeyboardMarkup = createShelterInfoMenu(
                SHELTER_INFO.getText(), SCHEDULE.getText(), SECURITY_CONTACTS.getText(),
                SAFETY_RECOMMENDATION.getText(), APPLICATION.getText(), VOLUNTEER.getText()
        );

        SendMessage sendMessage = new SendMessage(chatId, "Подробная информация о приюте кошек")
                .replyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }



    @Override
    public void runMenuForAdopter(Long chatId) {
        // Кнопки меню усыновителя
        InlineKeyboardButton reportButton = new InlineKeyboardButton(REPORT.getText());
        reportButton.callbackData(REPORT.toString());

        InlineKeyboardButton anotherPetButton = new InlineKeyboardButton(ANOTHER_PET.getText());
        anotherPetButton.callbackData(ANOTHER_PET.toString());

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        // Добавление кнопок в клавиатуру
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup
                .addRow(reportButton)
                .addRow(anotherPetButton)
                .addRow(volunteerButton);

        // Создание сообщения, добавление в него клавиатуры с рядом кнопок
        SendMessage sendMessage = new SendMessage(chatId, "Меню действующего усыновителя");
        sendMessage.replyMarkup(inlineKeyboardMarkup);

        // Отправка сообщения
        SendResponse sendResponse = telegramBot.execute(sendMessage);
    }

    //метод для отправки *.pdf файла юзеру
    @Override
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
}

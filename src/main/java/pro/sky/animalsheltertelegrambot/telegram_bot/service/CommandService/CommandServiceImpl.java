package pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.RegularUserStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService.CallbackService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;

/**
 * Сервис для обработки команд в Telegram боте.
 * Регистрирует и обрабатывает команды, например, /start, а также действия, связанные с inline-кнопками.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final TelegramBot telegramBot;
    private final CallbackService callbackService;
    private final MessageSendingService messageSendingService;

    /**
     * Обрабатывает событие начала взаимодействия с пользователем (/start).
     * Отправляет пользователю приветственное сообщение с меню выбора приюта.
     * @param event событие начала команды, содержит идентификатор чата.
     */
    @EventListener
    public void handleStartCommandEvent(RegularUserStartEvent event) {
        log.info("Показ основного меню для chatId: {}", event.getChatId());
        SendMessage sendMessage = executeStartCommandIfUserExists(event.getChatId());
        messageSendingService.sendMessage(sendMessage);
    }


    /**
     * Обработка колбэк-сообщений, полученных от callback-кнопок inline-клавиатуры.
     * @param callbackQuery колбэк-запрос от кнопки inline-клавиатуры.
     */
    @Override
    public void receivedCallbackMessage(CallbackQuery callbackQuery) {
        callbackService.processCallback(callbackQuery);
        log.info("Получено и обработано колбэк-сообщение: {}", callbackQuery.message());
    }

    /**
     * Создает и отправляет основное меню при старте взаимодействия с пользователем.
     * @param chatId Идентификатор чата пользователя в Telegram.
     * @return Сообщение с inline-клавиатурой, содержащей выбор приюта.
     */
    @Override
    public SendMessage executeStartCommandIfUserExists(Long chatId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("DOGS  \uD83D\uDC36").callbackData("DOGS"),
                new InlineKeyboardButton("CATS \uD83D\uDC31").callbackData("CATS"));
        String testStr = "Выберите приют: \uD83D\uDC3E";
        SendMessage sendMessage = new SendMessage(chatId, testStr).replyMarkup(inlineKeyboard);
        log.info("Создано стартовое сообщение для chatId: {}", chatId);
        return sendMessage;
    }

    @Override
    public InlineKeyboardMarkup createShelterInfoMenu(Button... buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Button button : buttons) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(button.getText())
                    .callbackData(button.getCommand());
            inlineKeyboardMarkup.addRow(inlineButton);
        }
        return inlineKeyboardMarkup;
    }


    //==================================================================first menu=====================================
    /**
     * Создает и отправляет меню первого уровня для выбора собак в приюте.
     * @param chatId идентификатор чата, в который будет отправлено сообщение.
     * @param text текст, который будет отображаться в сообщении вместе с меню.
     * @return объект SendMessage, который содержит информацию для отправки сообщения через Telegram API.
     */
    @Override
    public SendMessage firstMenuDog(Long chatId, String text) {
        log.info("Создание первого уровня меню для собак для chatId: {}", chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER.getText())
                .callbackData(ABOUT_SHELTER.toString());
        InlineKeyboardButton applicationButton = new InlineKeyboardButton(APPLICATION_DOG.getText())
                .callbackData(APPLICATION_DOG.toString());
        InlineKeyboardButton adoptAnimalButton = new InlineKeyboardButton(HOW_TO_TAKE_PET.getText())
                .callbackData(HOW_TO_TAKE_PET.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText())
                .callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton, adoptAnimalButton);
        inlineKeyboardMarkup.addRow(applicationButton, volunteerButton);

        return new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
    }

    /**
     * Создает и отправляет меню первого уровня для выбора кошек в приюте.
     * @param chatId идентификатор чата, в который будет отправлено сообщение.
     * @param text текст, который будет отображаться в сообщении вместе с меню.
     * @return объект SendMessage, который содержит информацию для отправки сообщения через Telegram API.
     */
    @Override
    public SendMessage firsMenuCat(Long chatId, String text) {
        log.info("Создание первого уровня меню для кошек для chatId: {}", chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER_CAT.getText()).callbackData(ABOUT_SHELTER_CAT.toString());
        InlineKeyboardButton applicationButton = new InlineKeyboardButton(APPLICATION_CAT.getText()).callbackData(APPLICATION_CAT.toString());
        InlineKeyboardButton adoptAnimalButton = new InlineKeyboardButton(HOW_TO_TAKE_PET.getText()).callbackData(HOW_TO_TAKE_PET.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText()).callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton, adoptAnimalButton);
        inlineKeyboardMarkup.addRow(applicationButton, volunteerButton);

        return new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
    }

    //===============================================second menu===========================================================
    /**
     * Создает и отправляет подробное меню информации о приюте.
     * @param chatId идентификатор чата, в который будет отправлено сообщение.
     * @return объект SendMessage, который содержит информацию для отправки сообщения через Telegram API.
     */
    @Override
    public SendMessage runMenuShelterInfo(Long chatId) {
        log.info("Создание подробного меню о приюте для chatId: {}", chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton(SHELTER_INFO.getText()).callbackData(SHELTER_INFO.toString());
        InlineKeyboardButton scheduleButton = new InlineKeyboardButton(SCHEDULE.getText()).callbackData(SCHEDULE.toString());
        InlineKeyboardButton securityContactsButton = new InlineKeyboardButton(SECURITY_CONTACTS.getText()).callbackData(SECURITY_CONTACTS.toString());
        InlineKeyboardButton safetyRecommendationButton = new InlineKeyboardButton(SAFETY_RECOMMENDATION.getText()).callbackData(SAFETY_RECOMMENDATION.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText()).callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(shelterInfoButton, scheduleButton);
        inlineKeyboardMarkup.addRow(securityContactsButton, safetyRecommendationButton);
        inlineKeyboardMarkup.addRow(volunteerButton);

        return new SendMessage(chatId, "Подробное меню о приюте").replyMarkup(inlineKeyboardMarkup);
    }


    /**
     * Создает и отправляет меню для усыновителей животных.
     * Это меню предоставляет опции для отправки отчетов, выбора другого животного и волонтерства.
     *
     * @param chatId идентификатор чата пользователя, который является усыновителем.
     */
    @Override
    public void runMenuForAdopter(Long chatId, String userName) {
        log.info("Создание меню для усыновителя для chatId: {}", chatId);
        InlineKeyboardButton reportButton = new InlineKeyboardButton(REPORT.getText()).callbackData(REPORT.toString());
        InlineKeyboardButton anotherPetButton = new InlineKeyboardButton(ANOTHER_PET.getText()).callbackData(ANOTHER_PET.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText()).callbackData(VOLUNTEER.toString());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(reportButton).addRow(anotherPetButton).addRow(volunteerButton);

        String greeting = userName + "! ";
        String menuText = greeting + "Меню действующего усыновителя.";

        SendMessage sendMessage = new SendMessage(chatId, menuText).replyMarkup(inlineKeyboardMarkup);

        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (sendResponse.isOk()) {
            log.info("Меню усыновителя успешно отправлено для chatId: {}", chatId);
        } else {
            log.error("Ошибка при отправке меню усыновителя для chatId: {}: {}", chatId, sendResponse.description());
        }
    }
}

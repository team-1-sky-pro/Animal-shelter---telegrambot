package pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;

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
import pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.StartCommandEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService.CallbackService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final TelegramBot telegramBot;
    private final CallbackService callbackService;
    private final MessageSendingService messageSendingService;


    @EventListener
    public void onUserCreated(StartCommandEvent event) {
        // Реакция на событие, например, показать меню приюта
        firstMenuDog(event.getChatId(), "Выберите приют:");
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
        log.info("Отправили сообщение о выборе приюта chatId: {}", chatId);
        return sendMessage;
    }

    public InlineKeyboardMarkup createShelterInfoMenu(Button... buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Button button : buttons) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(button.getText())
                    .callbackData(button.getCommand());
            inlineKeyboardMarkup.addRow(inlineButton);
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


    //==================================================================first menu=====================================
    @Override
    public SendMessage firstMenuDog(Long chatId, String text) {
        log.info("Вызывается метод firstMenuDog");
        //кнопки для основного меню
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER.getText());
        aboutShelterButton.callbackData(ABOUT_SHELTER.toString());
        InlineKeyboardButton applicationButton = new InlineKeyboardButton(Button.APPLICATION.getText());
        applicationButton.callbackData(Button.APPLICATION.toString());
        InlineKeyboardButton adoptAnimalButton = new InlineKeyboardButton(ADOPT_ANIMAL_DOG.getText());
        adoptAnimalButton.callbackData(ADOPT_ANIMAL_DOG.toString());

        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton, adoptAnimalButton);
        inlineKeyboardMarkup.addRow(applicationButton, volunteerButton);
        return new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public SendMessage firsMenuCat(Long chatId, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton aboutShelterButton = new InlineKeyboardButton(ABOUT_SHELTER_CAT.getText());
        aboutShelterButton.callbackData(ABOUT_SHELTER_CAT.toString());
        InlineKeyboardButton applicationButton = new InlineKeyboardButton(Button.APPLICATION.getText());
        applicationButton.callbackData(Button.APPLICATION.toString());
        InlineKeyboardButton adoptAnimalButton = new InlineKeyboardButton(ADOPT_ANIMAL_CAT.getText());
        adoptAnimalButton.callbackData(ADOPT_ANIMAL_CAT.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(aboutShelterButton, adoptAnimalButton);
        inlineKeyboardMarkup.addRow(applicationButton, volunteerButton);
        return new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
    }

    //===============================================second menu===========================================================
    @Override
    public SendMessage runMenuShelterInfo(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton(SHELTER_INFO.getText());
        shelterInfoButton.callbackData(SHELTER_INFO.toString());
        InlineKeyboardButton scheduleButton = new InlineKeyboardButton(SCHEDULE.getText());
        scheduleButton.callbackData(Button.SCHEDULE.toString());
        InlineKeyboardButton securityContactsButton = new InlineKeyboardButton(SECURITY_CONTACTS.getText());
        securityContactsButton.callbackData(SECURITY_CONTACTS.toString());
        InlineKeyboardButton safetyRecommendationButton = new InlineKeyboardButton(SAFETY_RECOMMENDATION.getText());
        safetyRecommendationButton.callbackData(SAFETY_RECOMMENDATION.toString());
        InlineKeyboardButton volunteerButton = new InlineKeyboardButton(VOLUNTEER.getText());
        volunteerButton.callbackData(VOLUNTEER.toString());

        inlineKeyboardMarkup.addRow(shelterInfoButton, scheduleButton);
        inlineKeyboardMarkup.addRow(securityContactsButton, safetyRecommendationButton);
        inlineKeyboardMarkup.addRow(volunteerButton);

        return new SendMessage(chatId, "Подробное меню о приюте").replyMarkup(inlineKeyboardMarkup);
    }


    // Меню после нажатия кнопки "О приюте" -> попадаем в меню подробной информации


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

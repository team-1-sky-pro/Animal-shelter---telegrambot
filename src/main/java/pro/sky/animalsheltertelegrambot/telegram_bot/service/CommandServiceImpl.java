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
import org.springframework.stereotype.Service;

import pro.sky.animalsheltertelegrambot.service.*;


import java.util.regex.Pattern;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CallbackService callbackService;


    @Override
    public void processTextMessage(Long chatId, String text) {
        if ("/start".equals(text)) {
            userService.handleStart(chatId);
        } else {
            // Обработка других текстовых сообщений
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


    @Override
    public SendMessage runMainMenu(Long chatId, String text) {
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

    @Override
    public void processStartCommand(Long chatId, String s) {

    }

    //метод для отправки *.pdf файла юзеру
    @Override
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
}

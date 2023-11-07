package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import com.pengrad.telegrambot.request.SendMessage;

import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;


import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final String reportInfo = "Чтобы отправить отчет. Вам нужно в одном сообщении прикрепить фото питомца, " +
            "указать его ID и далее через точку описать его состояние.\n";
    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;


    @Override
    public SendMessage executeStartCommandIfUserExists(Long chatId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("DOGS  \uD83D\uDC36").callbackData("DOGS"),
                new InlineKeyboardButton("CATS \uD83D\uDC31").callbackData("CATS"));
        String testStr = "Выберите приют: \uD83D\uDC3E";
        SendMessage sendMessage = new SendMessage(chatId, testStr).replyMarkup(inlineKeyboard);
        return sendMessage;
    }

    @Override
    public void receivedCallbackMessage(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.data();
        Long chatId = callbackQuery.from().id();
        SendMessage sendMessage;
        switch (call_data) {
            case "CATS":
                sendMessage = runMainMenu(chatId, " Приют для кошек");
                telegramBot.execute(sendMessage);
                break;
            case "DOGS":
                sendMessage = runMainMenu(chatId, "Приют для собак");
                telegramBot.execute(sendMessage);
                break;
            case "ABOUT_SHELTER":
                telegramBot.execute(runMenuShelterInfo(chatId));
                break;
            case "DOG_SHELTER_INFO":
                telegramBot.execute(displayDogShelterContacts(chatId));
                break;
            case "SECURITY_CONTACTS":
                telegramBot.execute(displayDogShelterSecurityContacts(chatId));
                break;
            case "SCHEDULE":
                telegramBot.execute(displayDogShelterWorkingHours(chatId));
                break;
            case "REPORT":
                telegramBot.execute(displayReportInfo(chatId));
                break;
        }
    }

    @Override
    public SendMessage runMainMenu(Long chatId, String text) {

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
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton dogShelterInfo = new InlineKeyboardButton(DOG_SHELTER_INFO.getText());
        dogShelterInfo.callbackData(DOG_SHELTER_INFO.toString());

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

    @Override
    public SendMessage displayDogShelterContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findDogShelterContactsByShelterType());
        return sendMessage;
    }

    @Override
    public SendMessage displayDogShelterSecurityContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findDogShelterSecurityContactsByShelterType());
        return sendMessage;
    }

    @Override
    public SendMessage displayDogShelterWorkingHours(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findDogShelterWorkingHoursByShelterType());
        return sendMessage;
    }

    @Override
    public SendMessage displayReportInfo(Long chatId) {
        return new SendMessage(chatId, reportInfo);
    }

//    @Override
//    public String sendFileToUser(Long chatId) {
//        String file = "C:/Users/user/Desktop/IT/dog_shelter_info_.pdf";
//        String fileLink;
//        byte[] data = DatatypeConverter.parseBase64Binary(file);
//        SendDocument sendDocument = new SendDocument(chatId, data).fileName("dog_shelter_info_.pdf");
//        try {
//            SendResponse execute = telegramBot.execute(sendDocument);
//            Document document = execute.message().document();
//            final String documentId = document.fileId();
//            fileLink = getFileLink(documentId);
//            return fileLink;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String getFileLink(String fileId) {
//        GetFile getFile = new GetFile(fileId);
//        GetFileResponse fileResponse = telegramBot.execute(getFile);
//        File file = fileResponse.file();
//        log.info("getRelativeFilePath filePath : {C:/Users/user/Desktop/IT/}", file.filePath());
//        return telegramBot.getFullFilePath(file);
//    }

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
    public void saveReport(Message message) {

    }
}

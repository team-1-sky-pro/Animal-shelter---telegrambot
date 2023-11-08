package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final Pattern reportPattern = Pattern.compile("\\d+\\.\\s?[а-яА-Яa-zA-Z]+");
    private final String reportInfo = "Чтобы отправить отчет. Вам нужно в одном сообщении прикрепить фото питомца, " +
            "указать его ID и далее через точку описать его состояние.\n";
    private final TelegramBot telegramBot;
    private final ShelterRepository shelterRepository;
    private final PetService petService;
    private final PhotoService photoService;
    private final ReportService reportService;

    @Override
    public SendMessage executeStartCommandIfUserExists(Long chatId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("DOGS  \uD83D\uDC36").callbackData("DOGS"),
                new InlineKeyboardButton("CATS \uD83D\uDC31").callbackData("CATS"));
        String testStr = "Выберите приют: \uD83D\uDC3E";
        SendMessage sendMessage = new SendMessage(chatId, testStr).replyMarkup(inlineKeyboard);
        return sendMessage;
    }
    //метод для обработки callbackQuery
    @Override
    public void receivedCallbackMessage(CallbackQuery callbackQuery) {
        String call_data = callbackQuery.data();
        Long chatId = callbackQuery.from().id();
        SendMessage sendMessage;
        String path = "";

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
                path = "src/main/resources/files/dog_shelter_info_.pdf";
                sendDocument(path,chatId);
                break;
            case "SECURITY_CONTACTS":
                telegramBot.execute(displayDogShelterSecurityContacts(chatId));
                path = "src/main/resources/files/dog_shelter_security_contacts.pdf";
                sendDocument(path,chatId);
                break;
            case "SCHEDULE":
                telegramBot.execute(displayDogShelterWorkingHours(chatId));
                path = "src/main/resources/files/dog_shelter_schedule_address.pdf";
                sendDocument(path,chatId);
                break;
            case "SAFETY_RECOMMENDATION":
                path = "src/main/resources/files/dog_safety_recommendation.pdf";
                sendDocument(path,chatId);
                break;
            case "REPORT":
                telegramBot.execute(displayReportInfo(chatId));
                break;
        }
    }

    @Override
    public SendMessage runMainMenu(Long chatId, String text) {
        //кнопки для основного меню
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
    //метод для получения информации о приюте из бд
    @Override
    public SendMessage displayDogShelterContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId,  shelterRepository.findDogShelterContactsByShelterType() +"\nБолее подробная информация о приюте в файле:" );
        return sendMessage;
    }
    //метод для получения информации о контактах охраны из бд
    @Override
    public SendMessage displayDogShelterSecurityContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findDogShelterSecurityContactsByShelterType() + "\nБолее подробная информация в файле:");
        return sendMessage;
    }
    //метод для получения информации о часах работы, схемы проезда и адреса из бд
    @Override
    public SendMessage displayDogShelterWorkingHours(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterRepository.findDogShelterWorkingHoursByShelterType() + "\nБолее подробная информация в файле:");
        return sendMessage;
    }

    @Override
    public SendMessage displayReportInfo(Long chatId) {
        return new SendMessage(chatId, reportInfo);
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
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
    @Override
    public void saveReport(Message message) {
        Long chatId = message.chat().id();
        String text = message.caption();

        Matcher matcher = reportPattern.matcher(text);
        if (!matcher.matches()) {
            telegramBot.execute(new SendMessage(chatId, "Ошибка. Убедитесь, что заполнили текст отчета корректно."));
            return;
        }
        Long petId = Long.valueOf(text.substring(0, text.indexOf(".")));
        String reportText = text.substring(text.indexOf(".") + 1);

        GetFile getFileRequest = new GetFile(message.photo()[1].fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        try {
            File file = getFileResponse.file();

            if (!petService.existsById(petId)) {
                telegramBot.execute(new SendMessage(chatId, "Ошибка. У вас нет питомца с таким ID."));
                return;
            }
            Pet pet = new Pet();
            pet.setId(petId);
            Report report = new Report();
            report.setPetId(pet);
            report.setDateTime(LocalDateTime.now());
            report.setReportText(reportText);

            Photo photo = new Photo();
            photo.setFilePath(file.filePath());
            photo.setReport(report);
            photo.setFileSize(Long.valueOf(file.fileSize()));
            photo.setMediaType(getFileRequest.getContentType());

            reportService.addReport(report);
            photoService.addPhotoForReport(photo);
            telegramBot.execute(new SendMessage(chatId, "Отчет успешно отправлен!"));
        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка. Попробуйте еще раз."));
        }
    }
}

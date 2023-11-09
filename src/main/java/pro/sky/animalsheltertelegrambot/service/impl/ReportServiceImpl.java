package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;
import pro.sky.animalsheltertelegrambot.exception.ReportNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.ReportRepository;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.service.ReportService;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final TelegramBot telegramBot;
    private final PetService petService;
    private final PhotoService photoService;
    private final Pattern reportPattern = Pattern.compile("\\d+\\.\\s?[а-яА-Яa-zA-Z]+");

    private final String reportInfo = "Чтобы отправить отчет. Вам нужно в одном сообщении прикрепить фото питомца, " +
            "указать его ID и далее через точку описать его состояние.\n";

    /**
     * Добавление отчета.
     *
     * @param report Добавить отчет. Не должен быть null.
     */
    @Override
    public void addReport(Report report) {
        if (report == null) {
            log.info("Attempt to add a null adoption");
            throw new IllegalArgumentException("Report cannot be null");
        }
        log.info("Adding new report: {}", report);
        reportRepository.save(report);
    }

    /**
     * Получение отчета по ID.
     *
     * @param id ID отчета.
     * @return Найденный отчет или null, если отчет не найден.
     * @throws ReportNotFoundException Если усыновление не обнаружено.
     * @throws IllegalArgumentException Если Id равен null.
     */
    @Override
    public Report getReport(Long id) {
        if (id == null) {
            log.info("Attempt to add a null Id");
            throw new IllegalArgumentException("Report ID cannot be null");
        }
        log.info("Fetching Report with ID: {}", id);
        return reportRepository.findById(id).orElseThrow(
                ()-> new ReportNotFoundException("Report this Id not found."));
    }

    /**
     * Обновление отчета по ID.
     *
     * @param id ID отчета для обновления.
     * @param report Новые данные отчета. Не должны быть null.
     * @throws ReportNotFoundException Если усыновление не обнаружено.
     * @throws IllegalArgumentException Если Id равен null.
     */
    @Override
    public void updateReport(Long id, Report report) {
        if (id == null || report == null) {
            log.error("An attempt to update a report with null data");
            throw new IllegalArgumentException("ID and report cannot be null");
        }
        if (!reportRepository.existsById(id)) {
            log.error("Report with ID {} not found", id);
            throw new ReportNotFoundException("Report not found");
        }
        Report updateReport = getReport(id);
        updateReport.setDateTime(report.getDateTime());
        updateReport.setReportText(report.getReportText());

        reportRepository.save(updateReport);
    }

    /**
     * Удаление отчета по ID.
     *
     * @param id ID отчета для удаления.
     * @throws ReportNotFoundException Если усыновление не обнаружено.
     * @throws IllegalArgumentException Если Id равен null.
     */
    @Override
    public void deleteReport(Long id) {
        if (id == null) {
            log.error("Attempting to delete a report with a null ID");
            throw new IllegalArgumentException("ID and report cannot be null");
        }
        if (!reportRepository.existsById(id)) {
            log.error("Report with ID {} not found", id);
            throw new ReportNotFoundException("Report not found");
        }
        reportRepository.deleteById(id);
    }

    @Override
    public SendMessage displayReportInfo(Long chatId) {
        return new SendMessage(chatId, reportInfo);
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

            addReport(report);
            photoService.addPhotoForReport(photo);
            telegramBot.execute(new SendMessage(chatId, "Отчет успешно отправлен!"));
        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.execute(new SendMessage(chatId, "Произошла ошибка. Попробуйте еще раз."));
        }
    }

}

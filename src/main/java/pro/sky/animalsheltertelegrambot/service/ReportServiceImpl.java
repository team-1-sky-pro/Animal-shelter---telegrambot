package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.exception.ReportNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.model.ReportDTO;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;
import pro.sky.animalsheltertelegrambot.repository.PhotoRepository;
import pro.sky.animalsheltertelegrambot.repository.ReportRepository;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.ReportStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final TelegramBot telegramBot;
    private final PetRepository petRepository;
    private final PhotoRepository photoRepository;
    private final MessageSendingService messageSendingService;

    private final Path photosDir = Paths.get("./photos");
    private final Pattern reportPattern = Pattern.compile("\\d+\\.\\s?.*");
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

    @Override
    public ReportDTO getReportWithPhotos(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Отчет с данным ID не найден"));

        List<Photo> photos = photoRepository.findByReportId(id);
        List<String> photoIds = photos.stream()
                .map(photo -> String.valueOf(photo.getId()))
                .collect(Collectors.toList());

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(report.getId());
        reportDTO.setReportText(report.getReportText());
        reportDTO.setPhotoIds(photoIds);

        return reportDTO;
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
    public List listAllReport() {
        return reportRepository.findAll();
    }

    @Override
    public void displayReportInfo(Long chatId) {
       messageSendingService.sendMessage(chatId, reportInfo);
    }

    @EventListener
    private void onReportStartEvent(ReportStartEvent event) {
        saveReport(event.getChatId(), event.getText(), event.getFileId());
    }

    @Override
    public void saveReport(Long chatId, String caption, PhotoSize[] photoSizes) {
        log.info("Обработка отчета для chatId: {}", chatId);
        Matcher matcher = reportPattern.matcher(caption);
        if (!matcher.matches()) {
            log.error("Некорректное содержимое в заголовке для chatId: {}", chatId);
            telegramBot.execute(new SendMessage(chatId, "Ошибка. Убедитесь, что заполнили текст отчета корректно."));
            return;
        }

        Long petId = Long.valueOf(caption.substring(0, caption.indexOf(".")));
        String reportText = caption.substring(caption.indexOf(".") + 1);
        log.debug("Текст отчета: {}", reportText);

        PhotoSize photoSize = photoSizes[photoSizes.length - 1];
        GetFile getFileRequest = new GetFile(photoSize.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);

        if (getFileResponse.isOk()) {
            File file = getFileResponse.file();
            String fullFilePath = telegramBot.getFullFilePath(file);

            try (InputStream fileStream = new URL(fullFilePath).openStream()) {
                String fileName = file.fileId() + ".jpg";
                Path pathToSave = photosDir.resolve(fileName);
                Files.createDirectories(photosDir);
                Files.copy(fileStream, pathToSave, StandardCopyOption.REPLACE_EXISTING);
                log.info("Фотография сохранена по пути: {}", pathToSave);

                Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Pet not found"));
                Report report = new Report();
                report.setPetId(pet);
                report.setDateTime(LocalDateTime.now());
                report.setReportText(reportText);
                reportRepository.save(report);
                log.info("Отчет сохранен с ID: {}", report.getId());

                Photo photo = new Photo();
                photo.setFilePath(pathToSave.toString());
                photo.setFileSize((long) photoSize.fileSize());
                photo.setMediaType("image/jpeg"); // Или определяем MIME-тип динамически
                photo.setReport(report);
                photo.setPet(pet);
                photoRepository.save(photo);
                log.info("Запись фотографии сохранена с ID: {}", photo.getId());


                telegramBot.execute(new SendMessage(chatId, "Отчет успешно отправлен!"));
                log.info("Подтверждение отправлено пользователю с chatId: {}", chatId);
            } catch (IOException e) {
                e.printStackTrace();
                telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при сохранении фотографии."));
                log.error("Ошибка при сохранении фотографии для chatId: {}", chatId, e);
            }
        } else {
            telegramBot.execute(new SendMessage(chatId, "Не удалось получить информацию о файле."));
            log.error("Не удалось получить информацию о файле для chatId: {}", chatId);
        }
    }
}

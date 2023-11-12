package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.model.ReportDTO;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.ReportStartEvent;

import java.util.List;

public interface ReportService {
    void addReport(Report report);
    Report getReport(Long id);
    void updateReport(Long id, Report report);
    void deleteReport(Long id);
    void saveReport(Long chatId, String text, PhotoSize[]  photoSizes);
    void displayReportInfo(Long chatId);

    List listAllReport();

    ReportDTO getReportWithPhotos(Long id);
}

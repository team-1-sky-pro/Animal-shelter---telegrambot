package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Report;

@Service
public interface ReportService {
    void addReport(Report report);
    Report getReport(Long id);
    void updateReport(Long id, Report report);
    void deleteReport(Long id);

    public void saveReport(Message message);
    public SendMessage displayReportInfo(Long chatId);
}

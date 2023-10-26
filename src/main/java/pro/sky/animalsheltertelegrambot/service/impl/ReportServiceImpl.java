package pro.sky.animalsheltertelegrambot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;
import pro.sky.animalsheltertelegrambot.exception.ReportNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.ReportRepository;
import pro.sky.animalsheltertelegrambot.service.ReportService;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

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
}

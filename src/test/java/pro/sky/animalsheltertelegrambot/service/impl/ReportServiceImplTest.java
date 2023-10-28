package pro.sky.animalsheltertelegrambot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.ReportRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;


public class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report report;
    private Long reportId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        report = new Report();
        report.setId(reportId);
        report.setDateTime(LocalDateTime.of(2023, 10, 22, 21, 57));
        report.setReportText("Test Report Text");
    }
    @Test
    void addReport_ValidReport_Success() {
        when(reportRepository.save(report)).thenReturn(report);
        reportService.addReport(report);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void getReport_ValidId_Success() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        Report fetchedReport = reportService.getReport(reportId);
        assertSame(report, fetchedReport);
    }

    @Test
    void updateReport_ValidId_Success() {
        Report updatedReport = new Report();
        updatedReport.setDateTime(LocalDateTime.of(2023, 10, 22, 21, 57));
        updatedReport.setReportText("Updated Report Text");

        when(reportRepository.existsById(reportId)).thenReturn(true);
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.save(report)).thenReturn(report);

        reportService.updateReport(reportId, updatedReport);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void deleteReport_ValidId_Success() {
        when(reportRepository.existsById(reportId)).thenReturn(true);
        reportService.deleteReport(reportId);
        verify(reportRepository, times(1)).deleteById(reportId);
    }
}

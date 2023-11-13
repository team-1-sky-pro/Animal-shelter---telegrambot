package pro.sky.animalsheltertelegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.ReportRepository;
import pro.sky.animalsheltertelegrambot.service.ReportServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportServiceImplTest {
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report report;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        report = new Report();
        report.setId(id);
        report.setDateTime(LocalDateTime.of(2023, 11, 22, 23, 57));
        report.setReportText("Test Report Text");
    }

    @Test
    void testAddReportValidReportSuccess() {
        when(reportRepository.save(report)).thenReturn(report);
        reportService.addReport(report);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void testGetReportValidIdSuccess() {
        when(reportRepository.findById(id)).thenReturn(Optional.of(report));
        Report fetchedReport = reportService.getReport(id);
        assertSame(report, fetchedReport);
    }

    @Test
    void testUpdateReportValidIdSuccess() {
        Report updatedReport = new Report();
        updatedReport.setDateTime(LocalDateTime.of(2023, 10, 22, 21, 57));
        updatedReport.setReportText("Updated Report Text");

        when(reportRepository.existsById(id)).thenReturn(true);
        when(reportRepository.findById(id)).thenReturn(Optional.of(report));
        when(reportRepository.save(report)).thenReturn(report);

        reportService.updateReport(id, updatedReport);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void testDeleteReportValidIdSuccess() {
        when(reportRepository.existsById(id)).thenReturn(true);
        reportService.deleteReport(id);
        verify(reportRepository, times(1)).deleteById(id);
    }

//    @Test
//    SendMessage testDisplayReportInfo() {
//    }

//    @Test
//    void testSaveReport() {
//    }

}

package pro.sky.animalsheltertelegrambot.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.model.ReportDTO;
import pro.sky.animalsheltertelegrambot.service.ReportService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;






@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    public void testAddReport() throws Exception {
        Report newReport = new Report();

        doNothing().when(reportService).addReport(any(Report.class));

        mockMvc.perform(post("/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newReport)))
                .andExpect(status().isOk());

        verify(reportService, times(1)).addReport(any(Report.class));
    }

    @Test
    public void testGetReportFound() throws Exception {
        Long reportId = 1L;
        ReportDTO reportDTO = new ReportDTO();

        when(reportService.getReportWithPhotos(reportId)).thenReturn(reportDTO);

        mockMvc.perform(get("/reports/{id}", reportId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testGetReportNotFound() throws Exception {
        Long reportId = 2L;
        when(reportService.getReportWithPhotos(reportId)).thenReturn(null);

        mockMvc.perform(get("/reports/{id}", reportId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("There is no such report with this: " + reportId)));
    }

    @Test
    public void testUpdateReport() throws Exception {
        Long reportId = 1L;
        Report updatedReport = new Report();

        doNothing().when(reportService).updateReport(eq(reportId), any(Report.class));

        mockMvc.perform(put("/reports/{id}", reportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedReport)))
                .andExpect(status().isOk());

        verify(reportService, times(1)).updateReport(eq(reportId), any(Report.class));
    }

    @Test
    public void testDeleteReport() throws Exception {
        Long reportId = 1L;

        doNothing().when(reportService).deleteReport(reportId);

        mockMvc.perform(delete("/reports/{id}", reportId))
                .andExpect(status().isOk());

        verify(reportService, times(1)).deleteReport(reportId);
    }

    @Test
    public void testGetAllReports() throws Exception {
        List<Report> reports = Arrays.asList(new Report(), new Report()); // Создайте список отчетов
        when(reportService.listAllReport()).thenReturn(reports);

        mockMvc.perform(get("/reports/allreports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(reports.size())));
    }

    @Test
    public void testGetAllReportsEmpty() throws Exception {
        when(reportService.listAllReport()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reports/allreports"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Not found reports")));
    }
}

package pro.sky.animalsheltertelegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService service;

    @PostMapping
    public void addReport(@RequestBody Report report) {
        service.addReport(report);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return new ResponseEntity<>(service.getReport(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public void updateReport(@PathVariable Long id, @RequestBody Report report) {
        service.updateReport(id, report);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        service.deleteReport(id);
    }
}

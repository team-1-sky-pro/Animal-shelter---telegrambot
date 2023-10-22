package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.service.ReportService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Отчеты", description = "Создание, редактирование и удаление отчетов по питомцам")
@RequestMapping("/reports")
public class ReportController {
    private final ReportService service;

    @Operation(
            summary = "Создание нового отчета",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новый отчет"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное создание"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PostMapping
    public void addReport(@RequestBody Report report) {
        service.addReport(report);
    }

    @Operation(
            summary = "Получение отчета из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет был успешно найден",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Report.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет по переданному id не был найден"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return new ResponseEntity<>(service.getReport(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Редактирование данных существующего в базе отчета",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Отредактированные отчет"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное обновление"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PutMapping("/{id}")
    public void updateReport(@PathVariable Long id, @RequestBody Report report) {
        service.updateReport(id, report);
    }

    @Operation(
            summary = "Удаление отчета из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет по переданному id не был найден"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        service.deleteReport(id);
    }
}

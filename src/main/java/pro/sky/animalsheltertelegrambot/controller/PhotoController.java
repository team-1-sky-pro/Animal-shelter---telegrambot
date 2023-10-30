package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalsheltertelegrambot.service.PhotoService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Фотографии", description = "Возможные операции с фотографиями, могут относиться либо к питомцу, либо к отчету")
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @Operation(
            summary = "Добавление фотографий питомца в базу",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Успешное добавление фотографий"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PostMapping(value = "for-pet/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addPhotosForPet(@Parameter(description = "ID питомца") @PathVariable Long petId,
                                @Parameter(description = "Загружаемые фотографии") @RequestBody MultipartFile[] photos) throws IOException {
        photoService.addPhotosForPet(petId, photos);
    }

    @Operation(
            summary = "Добавление фотографий для отчета в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешное добавление фотографий"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PostMapping(value = "for-report/{reportId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addPhotosForReport(@Parameter(description = "ID отчета") @PathVariable Long reportId,
                                   @Parameter(description = "Загружаемые фотографии") @RequestParam MultipartFile[] photos) throws IOException {
        photoService.addPhotosForReport(reportId, photos);
    }

    @Operation(
            summary = "Получение фотографий по ID питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотографии найдены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотографии не найдены"
                    )
            }
    )
    @GetMapping(value = "/for-pet/{petId}/{photoNumber}")
    public void getPhotosForPet(@Parameter(description = "ID питомца") @PathVariable Long petId,
                                @Parameter(description = "порядковый номер фотографии") @PathVariable Long photoNumber,
                                HttpServletResponse response) throws IOException {
        photoService.getPhotosByPetId(petId, photoNumber, response);
    }

    @Operation(
            summary = "Получение фотографий по ID приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотографии найдены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотографии не найдены"
                    )
            }
    )
    @GetMapping("/for-report/{reportId}/{photoNumber}")
    public void getPhotosForReport(@Parameter(description = "ID отчета") @PathVariable Long reportId,
                                   @Parameter(description = "порядковый номер фотографии") @PathVariable Long photoNumber,
                                   HttpServletResponse response) throws IOException {
        photoService.getPhotosByReportId(reportId, photoNumber, response);
    }

    @Operation(
            summary = "Удаление всех фотографий по ID приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "У питомца еще нет добавленных фотографий"
                    )
            }
    )
    @DeleteMapping("/for-pet/{petId}")
    public ResponseEntity<String> deleteAllPhotosByPetId(@Parameter(description = "ID питомца") @PathVariable Long petId) {
        photoService.deletePhotosByPetId(petId);
        return new ResponseEntity<>("All photos deleted for petId: " + petId, HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление всех фотографий по ID отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотографии успешно удалены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "У отчета еще нет добавленных фотографий"
                    )
            }
    )
    @DeleteMapping("/for-report/{reportId}")
    public ResponseEntity<String> deleteAllPhotosByReportId(@Parameter(description = "ID отчета") @PathVariable Long reportId) {
        photoService.deletePhotosByReportId(reportId);
        return new ResponseEntity<>("All photos deleted for reportId: " + reportId, HttpStatus.OK);
    }
}

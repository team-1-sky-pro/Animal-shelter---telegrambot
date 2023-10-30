package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Фотографии", description = "Возможные операции с фотографиями")
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @Operation(
            summary = "Добавление фотографий питомца в базу",
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
    @PostMapping(value = "for-pet/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPhotosForPet(@PathVariable Long petId, @RequestBody MultipartFile[] photos) throws IOException {
        photoService.addPhotosForPet(petId, photos);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
    public ResponseEntity<?> addPhotosForReport(@PathVariable Long reportId, @RequestParam MultipartFile[] photos) throws IOException {
        photoService.addPhotosForReport(reportId, photos);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получение фотографии по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография найдена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотография не найдена"
                    )
            }
    )
    @GetMapping("/for-pet/{petId}")
    public ResponseEntity<?> getPhotosForPet(@PathVariable Long petId,
                                             HttpServletResponse response) {
        photoService.getPhotosByPetId(petId, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получение фотографии по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография найдена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотография не найдена"
                    )
            }
    )
    @GetMapping("/for-report/{reportId}")
    public ResponseEntity<?> getPhotosForReport(@PathVariable Long reportId,
                                                HttpServletResponse response) {
        photoService.getPhotosByReportId(reportId, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление всех фотографий по Id приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотография не найдена"
                    )
            }
    )
    @DeleteMapping("/for-pet/{petId}")
    public ResponseEntity<String> deleteAllPhotosByPetId(@PathVariable Long petId) {
        photoService.deletePhotosByPetId(petId);
        return new ResponseEntity<>("All photos deleted for petId: " + petId, HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление всех фотографий по Id отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотографии успешно удалены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "У отчета еще нет фотографий"
                    )
            }
    )
    @DeleteMapping("/for-report/{reportId}")
    public ResponseEntity<String> deleteAllPhotosByReportId(@PathVariable Long reportId) {
        photoService.deletePhotosByReportId(reportId);
        return new ResponseEntity<>("All photos deleted for reportId: " + reportId, HttpStatus.OK);
    }
}

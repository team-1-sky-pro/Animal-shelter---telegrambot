package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.utils.ErrorUtils;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;

    @Operation(
            summary = "Добавление новой фотографии в базу",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Новая фотография"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешное добавление фотографии"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            },
            tags = "Photos"
    )
    @PostMapping
    public ResponseEntity<?> addPhoto(@Valid @RequestBody Photo photo, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo newPhoto = photoService.addPhoto(photo);
        return new ResponseEntity<>(newPhoto, HttpStatus.CREATED);
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
            },
            tags = "Photos"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        Photo existPhoto = photoService.getPhoto(id);
        if (existPhoto == null) {
            return new ResponseEntity<>("Photo not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existPhoto, HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление данных фотографии по ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Обновленные данные фотографии"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография успешно обновлена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности или фотография не найдена"
                    )
            },
            tags = "Photos"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhoto(@Valid @PathVariable Long id, @RequestBody Photo photo,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo updatePhoto = photoService.updatePhoto(id, photo);
        return new ResponseEntity<>(updatePhoto, HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление фотографии по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Фотография не найдена"
                    )
            },
            tags = "Photos"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return new ResponseEntity<>("Photo deleted " + id, HttpStatus.OK);
    }
}
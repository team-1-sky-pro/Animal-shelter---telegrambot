package pro.sky.animalsheltertelegrambot.controller;

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
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;

    /**
     * Добавляет новое фото в систему.
     *
     * @param photo Данные фотографии для добавления.
     * @param result BindingResult, содержащий результаты валидации.
     * @return ResponseEntity с добавленным фото или списком ошибок.
     */
    @PostMapping
    public ResponseEntity<?> addPhoto(@Valid @RequestBody Photo photo, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo newPhoto = photoService.addPhoto(photo);
        return new ResponseEntity<>(newPhoto, HttpStatus.CREATED);
    }

    /**
     * Получает фото по его ID.
     *
     * @param id ID фотографии для получения.
     * @return ResponseEntity с фотографией или сообщением об ошибке.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        Photo existPhoto = photoService.getPhoto(id);
        if (existPhoto == null) {
            return new ResponseEntity<>("Photo not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existPhoto, HttpStatus.OK);
    }

    /**
     * Обновляет данные фото по его ID.
     *
     * @param id ID фотографии для обновления.
     * @param photo Обновленные данные фотографии.
     * @param result BindingResult, содержащий результаты валидации.
     * @return ResponseEntity с обновленным фото или списком ошибок.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhoto(@Valid @PathVariable Long id, @RequestBody Photo photo,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo updatePhoto = photoService.updatePhoto(id, photo);
        return new ResponseEntity<>(updatePhoto, HttpStatus.OK);
    }

    /**
     * Удаляет фото по его ID.
     *
     * @param id ID фотографии для удаления.
     * @return ResponseEntity с подтверждающим сообщением.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return new ResponseEntity<>("Photo deleted " + id, HttpStatus.OK);
    }
}

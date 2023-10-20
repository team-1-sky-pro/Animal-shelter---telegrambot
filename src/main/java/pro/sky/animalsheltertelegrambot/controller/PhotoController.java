package pro.sky.animalsheltertelegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.utils.ErrorUtils;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {


    private final PhotoService photoService;


    @PostMapping
    public ResponseEntity<?> addPhoto(@RequestBody Photo photo, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo newPhoto = photoService.addPhoto(photo);
        return new ResponseEntity<>(newPhoto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable Long id) {
        Photo existPhoto = photoService.getPhoto(id);
        if (existPhoto == null) {
            return new ResponseEntity<>("Photo not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existPhoto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable Long id, @RequestBody Photo photo,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Photo updatePhoto = photoService.updatePhoto(id, photo);
        return new ResponseEntity<>(updatePhoto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return new ResponseEntity<>("Photo deleted " + id, HttpStatus.OK);
    }
}

package pro.sky.animalsheltertelegrambot.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.utils.ErrorUtils;

@RestController
@RequestMapping("/adoptions")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;


    /**
     * Добавляет новую запись о принятии животного на усыновление.
     *
     * @param adoption Объект, содержащий информацию о принятии на усыновление.
     * @param result   Объект, содержащий информацию об ошибках валидации объекта adoption.
     * @return ResponseEntity содержащий созданный объект Adoption в случае успешного создания
     * или список ошибок валидации в случае ошибки.
     *
     * @throws IllegalArgumentException если {@code adoption} является null или содержит неверные данные.
     *
     * @author Ваше имя или имя команды
     */
    @PostMapping
    public ResponseEntity<?> addAdoption(@RequestBody Adoption adoption, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Adoption newAdoption = adoptionService.addAdoption(adoption);
        return new ResponseEntity<>(newAdoption, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdoption(@PathVariable Long id) {
        Adoption existAdoption = adoptionService.getAdoption(id);
        if (existAdoption == null) {
            return new ResponseEntity<>("Adoption not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existAdoption, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdoption(@PathVariable Long id, @RequestBody Adoption adoption,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Adoption updateAdoption = adoptionService.updateAdoption(id, adoption);
        return new ResponseEntity<>(updateAdoption, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return new ResponseEntity<>("Adoption deleted " + id, HttpStatus.OK);
    }


}
package pro.sky.animalsheltertelegrambot.utils;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;

/**
 * Класс, который отлавливает прописанные в нем типы исключений и выполняет логику по их обработке.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("MaxUploadSizeExceededException occurred");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AdoptionNotFoundExceptions.class)
    public ResponseEntity<String> notFoundAdoptions(AdoptionNotFoundExceptions exceptions) {
        log.warn("AdoptionNotFoundExceptions occurred");
        return new ResponseEntity<>(exceptions.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

package pro.sky.animalsheltertelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,
                reason = "Pet was not found in the storage")
public class PetNotFoundException extends RuntimeException {
}

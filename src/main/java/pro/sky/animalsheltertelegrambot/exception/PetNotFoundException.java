package pro.sky.animalsheltertelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,
        reason = "Please choose different pet name, because this one is already taken")
public class PetNotFoundException extends RuntimeException {
}

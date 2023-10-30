package pro.sky.animalsheltertelegrambot.exception.photos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,
        reason = "Photos are not found")
public class PhotoNotFoundException extends RuntimeException {
}

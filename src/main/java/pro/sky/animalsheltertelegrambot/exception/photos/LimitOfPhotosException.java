package pro.sky.animalsheltertelegrambot.exception.photos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "You are at a quantity limit of photos, max = 9.")
public class LimitOfPhotosException extends RuntimeException {
}

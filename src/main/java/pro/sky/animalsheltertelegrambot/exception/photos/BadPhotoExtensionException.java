package pro.sky.animalsheltertelegrambot.exception.photos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "Extension of chosen file is now allowed")
public class BadPhotoExtensionException extends RuntimeException {
}

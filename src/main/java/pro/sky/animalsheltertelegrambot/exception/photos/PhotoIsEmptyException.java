package pro.sky.animalsheltertelegrambot.exception.photos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "For this operation you need to upload files in all lines")
public class PhotoIsEmptyException extends RuntimeException {
}

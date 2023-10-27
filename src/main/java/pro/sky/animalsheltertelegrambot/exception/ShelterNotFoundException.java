package pro.sky.animalsheltertelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ShelterNotFoundException extends IllegalArgumentException{
    public ShelterNotFoundException(String s) {
        super(s);
    }
}

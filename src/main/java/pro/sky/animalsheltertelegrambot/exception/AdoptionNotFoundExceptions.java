package pro.sky.animalsheltertelegrambot.exception;

public class AdoptionNotFoundExceptions extends IllegalArgumentException {
    public AdoptionNotFoundExceptions() {
    }

    public AdoptionNotFoundExceptions(String s) {
        super(s);
    }

    public AdoptionNotFoundExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}

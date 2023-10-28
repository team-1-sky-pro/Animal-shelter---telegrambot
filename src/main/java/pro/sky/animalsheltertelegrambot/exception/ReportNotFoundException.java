package pro.sky.animalsheltertelegrambot.exception;

public class ReportNotFoundException extends IllegalArgumentException {

    public ReportNotFoundException() {
    }

    public ReportNotFoundException(String s) {
        super(s);
    }

    public ReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

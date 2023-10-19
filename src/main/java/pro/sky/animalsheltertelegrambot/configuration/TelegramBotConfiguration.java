package pro.sky.animalsheltertelegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.request.BaseRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TelegramBotConfiguration класс отвечает за конфигурацию телеграм бота
 * @author Rnd-mi
 */
@Configuration
public class TelegramBotConfiguration {

    /**
     * Токен телеграм бота. Значение указано в application.properties
     */
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Метод создания {@link TelegramBot} бина. Для этого используется токен
     * и затем вызывается {@link TelegramBot#execute(BaseRequest)}
     * с пустым объектом класса {@link DeleteMyCommands}, который наследуется от {@link BaseRequest}
     * @return объект TelegramBot
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}

package pro.sky.animalsheltertelegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TelegramBotConfiguration class is responsible for configuring telegram bot
 * @author Rnd-mi
 */
@Configuration
public class TelegramBotConfiguration {

    /**
     * Retrieving token value from application.properties
     */
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Generating TelegramBot bean
     * @return TelegramBot instance
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}

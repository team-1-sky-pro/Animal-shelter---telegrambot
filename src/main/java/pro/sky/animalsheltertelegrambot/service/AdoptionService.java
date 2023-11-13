package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Adoption;

import java.util.List;

public interface AdoptionService {

    Adoption addAdoption(Adoption adoption);

    Adoption getAdoption(Long id);

    Adoption updateAdoption(Long id, Adoption adoption);

    void deleteAdoption(Long id);

    List allAdoptionIsFalse();

    /**
     * Сохраняет контактную информацию пользователя в базе данных.
     * Если пользователь с указанным идентификатором существует, обновляет его адрес электронной почты и номер телефона.
     * В случае отсутствия пользователя в базе данных, отправляет сообщение об ошибке через Telegram бота.
     *
     * @param userId      Идентификатор пользователя, чьи контактные данные необходимо обновить.
     * @param email       Адрес электронной почты пользователя.
     * @param phoneNumber Номер телефона пользователя.
     */
    void saveUserContactInfo(Long userId, String email, String phoneNumber);

    /**
     * Предлагает пользователю список животных, доступных для усыновления из указанного приюта.
     * Метод извлекает список животных, которые еще не усыновлены и для которых нет активных заявок на усыновление,
     * и отправляет этот список пользователю в виде кнопок в Telegram чате.
     *
     * @param chatId      Идентификатор чата пользователя, которому нужно предложить животных.
     * @param telegramBot Экземпляр бота Telegram, используемый для отправки сообщений.
     * @param shelterId   Идентификатор приюта, из которого предлагаются животные для усыновления.
     */
    void offerAnimalsToAdopt(Long chatId, TelegramBot telegramBot, Long shelterId);

    /**
     * Обрабатывает процесс усыновления животного в Telegram боте.
     * Метод проверяет доступность животного для усыновления. Если животное уже усыновлено или не существует,
     * отправляет соответствующее сообщение пользователю. В противном случае, начинает процесс усыновления,
     * отправляя пользователю детали о выбранном животном и предоставляя кнопку для подтверждения усыновления.
     *
     * @param chatId      Идентификатор чата пользователя, инициировавшего процесс усыновления.
     * @param petId       Идентификатор животного, выбранного для усыновления.
     * @param telegramBot Экземпляр бота Telegram, используемый для отправки сообщений.
     */
    void handleAnimalAdoption(Long chatId, Long animalId, TelegramBot telegramBot);

    boolean isAdoptionCallback(String data);

    /**
     * Обрабатывает колбэк-запросы от пользователей в Telegram боте, связанные с усыновлением животных.
     * Метод анализирует данные колбэка и выполняет соответствующие действия, такие как отображение информации о животном,
     * начало процесса усыновления, или обработка неизвестных колбэков.
     *
     * @param callbackQuery Колбэк запрос от пользователя.
     * @param telegramBot   Экземпляр бота Telegram, используемый для отправки сообщений.
     */
    void handleAdoptionCallback(CallbackQuery callbackQuery, TelegramBot telegramBot);

    void requestContactInfo(Long chatId);

    /**
     * Обрабатывает заявку на усыновление животного.
     * Метод проверяет, существует ли уже заявка на усыновление данного животного пользователем.
     * Если заявка уже существует, отправляет пользователю сообщение об этом и регистрирует попытку усыновления.
     * В противном случае создает новую заявку на усыновление и сохраняет ее в репозитории.
     *
     * @param chatId   Идентификатор чата пользователя, отправившего заявку.
     * @param animalId Идентификатор животного, на которое подается заявка.
     */
    void processContactInfo(Long chatId, String text, TelegramBot telegramBot);

    /**
     * Запускает процесс усыновления животного для пользователя в Telegram боте.
     * Вызывает метод для предложения животных, доступных для усыновления из указанного приюта,
     * и сохраняет предпочтение пользователя относительно приюта.
     *
     * @param chatId Идентификатор чата пользователя, начинающего процесс усыновления.
     * @param shelterId Идентификатор приюта, из которого пользователь хочет усыновить животное.
     */
    void startAdoptionProcess(Long chatId, Long shelterId);
}

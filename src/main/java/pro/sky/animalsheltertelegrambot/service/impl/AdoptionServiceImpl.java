package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса по управлению усыновлением.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final TelegramBot telegramBot;
    private final PetService petService;

    private final Map<Long, Long> userShelterPreference = new HashMap<>();
    private final Map<Long, Long> userSelectedPet = new HashMap<>();

    /**
     * Добавляет новое усыновление.
     *
     * @param adoption Усыновление, которую нужно добавить.
     * @return Добавлено усыновления.
     */
    @Override
    public Adoption addAdoption(Adoption adoption) {
        if (adoption == null) {
            log.error("Attempt to add a null adoption");
            return null;
        }

        log.info("Adding new adoption: {}", adoption);
        return adoptionRepository.save(adoption);
    }

    /**
     * Получает усыновление по его Id.
     *
     * @param id Id усыновления, который требуется получить.
     * @return Найденное усыновление.
     * @throws AdoptionNotFoundExceptions Если усыновление не обнаружено.
     * @throws IllegalArgumentException   Если Id равен null.
     */
    @Override
    public Adoption getAdoption(Long id) {
        if (id == null) {
            log.error("Attempt to get adoption with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }
        log.info("Fetching adoption with ID: {}", id);
        return adoptionRepository.findById(id).orElseThrow(
                () -> new AdoptionNotFoundExceptions("Adoption this Id not found."));
    }

    /**
     * Обновляет существующее усыновление.
     *
     * @param id       Id усыновления, который необходимо обновить.
     * @param adoption Усыновления с обновленными полями.
     * @return Обновленное усыновление.
     * @throws NullPointerException Если данные Id или усыновления равны null.
     */
    @Override
    public Adoption updateAdoption(Long id, Adoption adoption) {
        if (id == null || adoption == null) {
            log.error("Invalid ID or adoption data for update");
            throw new IllegalArgumentException("ID and Adoption cannot be null");
        }
        Adoption updatesAdoption = getAdoption(id);
        if (updatesAdoption == null) {
            log.error("Adoption with ID {} not found", id);
            throw new IllegalArgumentException("Updates Adoption and Adoption cannot be null");
        }
        updatesAdoption.setAdoptionDate(adoption.getAdoptionDate());
        updatesAdoption.setTrialEndDate(adoption.getTrialEndDate());
        updatesAdoption.setActive(adoption.isActive());

        log.info("Updating adoption with ID: {}", id);
        return adoptionRepository.save(updatesAdoption);
    }

    /**
     * Удаляет усыновление по его Id.
     *
     * @param id Id усыновления, которое необходимо удалить.
     * @throws AdoptionNotFoundExceptions Если усыновление не обнаружено.
     * @throws NullPointerException       Если Id равен null.
     */
    @Override
    public void deleteAdoption(Long id) {
        if (id == null) {
            log.error("Attempt to delete adoption with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (!adoptionRepository.existsById(id)) {
            log.error("Adoption with ID {} not found", id);
            throw new AdoptionNotFoundExceptions("Adoption this Id not found");
        }

        log.info("Deleting adoption with ID: {}", id);
        adoptionRepository.deleteById(id);
    }

    /**
     * Получает список всех неактивных усыновлений из репозитория усыновлений.
     * Метод извлекает и возвращает список усыновлений, которые помечены как неактивные.
     * В случае, если неактивные усыновления отсутствуют, выбрасывает исключение.
     *
     * @param shelterId Идентификатор приюта, для которого необходимо найти неактивные усыновления.
     * @return Список неактивных усыновлений.
     * @throws AdoptionNotFoundExceptions Если неактивные усыновления не найдены.
     */
    @Override
    public List allAdoptionIsFalse() {
        List<Adoption> inactiveAdoptions = adoptionRepository.findByIsActiveFalse();
        log.info("Найдено {} неактивных усыновлений.", inactiveAdoptions.size());
        if (inactiveAdoptions.isEmpty()) {
            log.info("Неактивные усыновления не найдены.");
            throw new AdoptionNotFoundExceptions("Нет не активных усыновлений");
        }

        log.info("Найдено {} неактивных усыновлений.", inactiveAdoptions.size());
        return inactiveAdoptions;
    }


    // ================================ Start of Adoption Process Methods ================================
    /**
     * Запускает процесс усыновления животного для пользователя в Telegram боте.
     * Вызывает метод для предложения животных, доступных для усыновления из указанного приюта,
     * и сохраняет предпочтение пользователя относительно приюта.
     *
     * @param chatId Идентификатор чата пользователя, начинающего процесс усыновления.
     * @param shelterId Идентификатор приюта, из которого пользователь хочет усыновить животное.
     */
    @Override
    public void startAdoptionProcess(Long chatId, Long shelterId) {
        offerAnimalsToAdopt(chatId, telegramBot, shelterId);
        userShelterPreference.put(chatId, shelterId);
    }

    /**
     * Запрашивает у пользователя введение контактной информации в Telegram боте.
     * Отправляет пользователю сообщение с инструкцией о том, как и в каком формате следует предоставить свою контактную информацию.
     *
     * @param chatId Идентификатор чата пользователя, у которого запрашивается контактная информация.
     */
    private void requestContactInfo(Long chatId) {
        String requestText = "Введите ваш email и номер телефона через запятую (например, email@example.com, +1234567890).";
        SendMessage requestMessage = new SendMessage(chatId, requestText);
        telegramBot.execute(requestMessage);
    }

    /**
     * Обрабатывает введенную пользователем контактную информацию в Telegram боте.
     * Проверяет формат введенных данных, соответствие электронной почты и номера телефона установленным критериям.
     * Если данные уникальны, сохраняет их и продолжает процесс усыновления животного, если таковой запущен.
     * В случае обнаружения дубликатов или некорректного формата данных, отправляет сообщение об ошибке пользователю.
     *
     * @param chatId Идентификатор чата пользователя, отправившего контактную информацию.
     * @param text Строка с контактной информацией пользователя.
     * @param telegramBot Экземпляр бота Telegram, используемый для отправки сообщений.
     */
    @Override
    public void processContactInfo(Long chatId, String text, TelegramBot telegramBot) {
        if (text.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,},\\s*\\+?\\d{10,15}$")) {
            String[] parts = text.split(",", 2);
            String email = parts[0].trim();
            String phoneNumber = parts[1].trim();

            boolean emailExists = userRepository.existsByEmail(email);
            boolean phoneExists = userRepository.existsByPhone(phoneNumber);

            if (emailExists) {
                String errorMessage = "Этот email уже используется. Пожалуйста, используйте другой.";
                SendMessage responseMessage = new SendMessage(chatId.toString(), errorMessage);
                telegramBot.execute(responseMessage);
                log.warn("Попытка использования существующего email для userId: {}.", chatId);
                return;
            }

            if (phoneExists) {
                String errorMessage = "Этот номер телефона уже используется. Пожалуйста, используйте другой.";
                SendMessage responseMessage = new SendMessage(chatId.toString(), errorMessage);
                telegramBot.execute(responseMessage);
                log.warn("Попытка использования существующего номера телефона для userId: {}.", chatId);
                return;
            }

            saveUserContactInfo(chatId, email, phoneNumber);
            log.info("Контактная информация для userId: {} успешно сохранена.", chatId);

            Long animalId = userSelectedPet.get(chatId);
            if (animalId != null) {
                processAdoptionApplication(chatId, animalId);
            }

        } else {
            String responseText = "Некорректный формат данных. Пожалуйста, введите их заново.";
            SendMessage responseMessage = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(responseMessage);
            log.info("Некорректный ввод данных от пользователя с userId: {}.", chatId);
        }
    }

    /**
     * Сохраняет контактную информацию пользователя в базе данных.
     * Если пользователь с указанным идентификатором существует, обновляет его адрес электронной почты и номер телефона.
     * В случае отсутствия пользователя в базе данных, отправляет сообщение об ошибке через Telegram бота.
     *
     * @param userId      Идентификатор пользователя, чьи контактные данные необходимо обновить.
     * @param email       Адрес электронной почты пользователя.
     * @param phoneNumber Номер телефона пользователя.
     */
    @Override
    public void saveUserContactInfo(Long userId, String email, String phoneNumber) {

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setEmail(email);
            user.setPhone(phoneNumber);
            userRepository.save(user);
            log.info("Контактные данные пользователя с userId: {} сохранены.", userId);
        } else {
            String errorMessage = "Ошибка сохранения данных. Проверьте данные и попробуйте снова.";
            SendMessage responseMessage = new SendMessage(userId.toString(), errorMessage);
            telegramBot.execute(responseMessage);
            log.error("Ошибка сохранения данных для userId: {}.", userId);
        }
    }

    /**
     * Предлагает пользователю список животных, доступных для усыновления из указанного приюта.
     * Метод извлекает список животных, которые еще не усыновлены и для которых нет активных заявок на усыновление,
     * и отправляет этот список пользователю в виде кнопок в Telegram чате.
     *
     * @param chatId      Идентификатор чата пользователя, которому нужно предложить животных.
     * @param telegramBot Экземпляр бота Telegram, используемый для отправки сообщений.
     * @param shelterId   Идентификатор приюта, из которого предлагаются животные для усыновления.
     */
    @Override
    public void offerAnimalsToAdopt(Long chatId, TelegramBot telegramBot, Long shelterId) {
        log.info("Вызвали метод offerAnimalsToAdopt для: " + chatId + " ");
        List<Pet> availableAnimals = petRepository.findAllByShelterIdAndIsAdoptedFalse(shelterId);

        List<Pet> animalsWithoutActiveAdoptions = availableAnimals.stream()
                .filter(pet -> !adoptionRepository.existsByPetId(pet.getId()))
                .toList();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Pet animal : animalsWithoutActiveAdoptions) {
            InlineKeyboardButton button = new InlineKeyboardButton(animal.getPetName())
                    .callbackData("ANIMAL_" + animal.getId());
            inlineKeyboardMarkup.addRow(button);
        }
        SendMessage message = new SendMessage(chatId, "Выберите животное для усыновления:")
                .replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(message);
    }

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
    @Override
    public void handleAnimalAdoption(Long chatId, Long petId, TelegramBot telegramBot) {
        Pet animal = petRepository.findById(petId).orElse(null);

        if (animal != null && animal.isAdopted()) {
            SendMessage message = new SendMessage(chatId, "Извините, это животное уже усыновлено или не существует.");
            log.warn("Adoption attempt for unavailable animalId: {}", petId);
            telegramBot.execute(message);
            return;
        }
        assert animal != null;
        petService.sendAnimalDetails(chatId, animal.getId());
        log.info("User {} started adoption process for animalId: {}", chatId, petId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton adoptButton = new InlineKeyboardButton("Выбрать")
                .callbackData("ADOPT_" + animal.getId());
        inlineKeyboardMarkup.addRow(adoptButton);

        SendMessage message = new SendMessage(chatId, "Если хотите усыновить, нажмите 'Выбрать'")
                .replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(message);
    }


    @Override
    public boolean isAdoptionCallback(String data) {
        return data != null && (data.startsWith("ANIMAL_") || data.startsWith("ADOPT_"));
    }

    /**
     * Обрабатывает колбэк-запросы от пользователей в Telegram боте, связанные с усыновлением животных.
     * Метод анализирует данные колбэка и выполняет соответствующие действия, такие как отображение информации о животном,
     * начало процесса усыновления, или обработка неизвестных колбэков.
     *
     * @param callbackQuery Колбэк запрос от пользователя.
     * @param telegramBot   Экземпляр бота Telegram, используемый для отправки сообщений.
     */
    @Override
    public void handleAdoptionCallback(CallbackQuery callbackQuery, TelegramBot telegramBot) {
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();

        if (callbackData.startsWith("ANIMAL_")) {
            Long animalId = Long.parseLong(callbackData.split("_")[1]);
            handleAnimalAdoption(chatId, animalId, telegramBot);
        } else if (callbackData.startsWith("ADOPT_")) {
            Long animalId = Long.parseLong(callbackData.split("_")[1]);
            User user = userRepository.findById(chatId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
            userSelectedPet.put(chatId, animalId);

            if (user.getPhone() == null || user.getEmail() == null) {
                requestContactInfo(chatId);
            } else {
                processAdoptionApplication(chatId, animalId);
            }

        } else {
            log.warn("Received unknown callback data: {}", callbackData);
            SendMessage message = new SendMessage(chatId, "Извините, мы не смогли распознать запрос.");
            telegramBot.execute(message);
        }
    }

    /**
     * Обрабатывает заявку на усыновление животного.
     * Метод проверяет, существует ли уже заявка на усыновление данного животного пользователем.
     * Если заявка уже существует, отправляет пользователю сообщение об этом и регистрирует попытку усыновления.
     * В противном случае создает новую заявку на усыновление и сохраняет ее в репозитории.
     *
     * @param chatId   Идентификатор чата пользователя, отправившего заявку.
     * @param animalId Идентификатор животного, на которое подается заявка.
     */
    private void processAdoptionApplication(Long chatId, Long animalId) {
        boolean alreadyAdopted = adoptionRepository.existsByUserIdAndPetId(chatId, animalId);
        if (alreadyAdopted) {
            SendMessage message = new SendMessage(chatId, "Извините, ваша заявка на это животное уже есть");
            log.warn("Adoption attempt for unavailable animalId: {}", animalId);
            telegramBot.execute(message);
        }

        Adoption adoption = new Adoption();
        adoption.setPetId(animalId);
        adoption.setUserId(chatId);
        adoption.setActive(false);
        adoption.setAdoptionDate(null);
        adoption.setTrialEndDate(null);

        adoptionRepository.save(adoption);

        String confirmationMessage = "Ваша заявка на усыновление отправлена. Ожидайте сообщения от волонтера приюта.";
        SendMessage confirmationSendMessage = new SendMessage(chatId.toString(), confirmationMessage);
        telegramBot.execute(confirmationSendMessage);
        log.info("Adoption application saved for user: {} and pet: {}", chatId, animalId);
    }

}

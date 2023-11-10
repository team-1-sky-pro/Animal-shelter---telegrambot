package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.animalsheltertelegrambot.model.Shelter;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса Приюта (Shelter)
 * базвые CRUD операции + просмотр всех приютов
 * @ addShelter - добавить приют
 * @ getShelter - получить приют по id
 * @ updateShelter - обновить приют/данные о приюте и т.п.
 * @ deleteShelter - удалить приют
 * @ getAllShelters - получить все Приюты
 * @ author SyutinS
 */

public interface ShelterService {
    Shelter addShelter(Shelter shelter);
    Optional<Shelter> getShelter(Long id);
    Shelter updateShelter(Long id, Shelter shelter);
    void deleteShelter(Long id);
    Collection<Shelter> getAllShelters();

    public SendMessage displayDogShelterWorkingHours(Long chatId);
    public SendMessage displayDogShelterSecurityContacts(Long chatId);
    public SendMessage displayDogShelterContacts(Long chatId);

    public String infoSheltonContact();
}

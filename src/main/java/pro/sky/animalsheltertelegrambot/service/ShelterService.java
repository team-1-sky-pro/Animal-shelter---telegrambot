package pro.sky.animalsheltertelegrambot.service;

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
}

package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalsheltertelegrambot.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    /**
     * метод, который находит приют по типу
     */
    @Query(value = "SELECT contacts FROM shelters WHERE shelter_type = 'cat'", nativeQuery = true)
    String findByShelterType();
}

package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalsheltertelegrambot.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    /**
     * метод, который находит адрес приюта по типу
     */
    @Query(value = "SELECT contacts FROM shelters WHERE shelter_type = 'dog'", nativeQuery = true)
    String findDogShelterContactsByShelterType();

//    @Query(value = "SELECT contacts FROM shelters WHERE shelter_type = 'cat'", nativeQuery = true)
//    String findCatShelterContactsByShelterType();

    @Query(value = "SELECT security_contacts FROM shelters WHERE shelter_type = 'dog'", nativeQuery = true)
    String findDogShelterSecurityContactsByShelterType();

//    @Query(value = "SELECT security_contacts FROM shelters WHERE shelter_type = 'cat'", nativeQuery = true)
//    String findCatShelterSecurityContactsByShelterType();

    @Query(value = "SELECT working_hours FROM shelters WHERE shelter_type = 'dog'", nativeQuery = true)
    String findDogShelterWorkingHoursByShelterType();

//    @Query(value = "SELECT working_hours FROM shelters WHERE shelter_type = 'cat'", nativeQuery = true)
//    String findCatShelterWorkingHoursByShelterType();

}

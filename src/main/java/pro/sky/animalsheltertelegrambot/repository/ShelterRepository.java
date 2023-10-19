package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalsheltertelegrambot.model.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

}

package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalsheltertelegrambot.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}

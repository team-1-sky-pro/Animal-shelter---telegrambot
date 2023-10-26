package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalsheltertelegrambot.model.Adoption;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
}

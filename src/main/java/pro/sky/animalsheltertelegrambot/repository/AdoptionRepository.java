package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalsheltertelegrambot.model.Adoption;

import java.util.List;
import java.util.Optional;

public interface AdoptionRepository extends JpaRepository<Adoption,Long> {
    @Query(value = "SELECT id FROM adoptions WHERE user_id=?1", nativeQuery = true)
    Long findIdByUserId(Long userId);
    @Query(value = "SELECT is_active FROM adoptions WHERE user_id=?1", nativeQuery = true)
    Optional<Boolean> checkAdoptionsIsActive(Long userId);

    boolean existsByUserIdAndPetId(Long chatId, Long animalId);
    boolean existsByPetId(Long petId);

    List<Adoption> findByIsActiveFalse();

}

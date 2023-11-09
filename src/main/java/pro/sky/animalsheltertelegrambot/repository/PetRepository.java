package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.animalsheltertelegrambot.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.isAdopted = false")
    List<Pet> findAllAvailableForAdoption();

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.photo WHERE p.id = :id")
    Optional<Pet> findByIdAndFetchPhoto(@Param("id") Long id);


    @Query("SELECT p FROM Pet p WHERE p.shelterId = :shelterId AND p.isAdopted = false")
    List<Pet> findAllByShelterIdAndIsAdoptedFalse(@Param("shelterId") Long shelterId);
}



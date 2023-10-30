package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.animalsheltertelegrambot.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query(value = "SELECT file_path FROM photos WHERE pet_id = :petId ORDER BY file_path DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastFilePathByPetId(@Param("petId") Long petId);

    @Query(value = "SELECT file_path FROM photos WHERE report_id = :reportId ORDER BY file_path DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastFilePathByReportId(@Param("reportId") Long reportId);

    List<Photo> findByPetId(Long petId);

    List<Photo> findByReportId(Long reportId);
}

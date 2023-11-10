package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalsheltertelegrambot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT id FROM users WHERE is_volunteer = true", nativeQuery = true)
    Long findUserIdIfUserIsVolunteer();
    @Query(value = "SELECT phone FROM users WHERE id = ?", nativeQuery = true)
    String findPhoneById(Long chatId);

    @Query(value = "SELECT user_name FROM users WHERE id = ?", nativeQuery = true)
    String findNameById(Long chatId);
}

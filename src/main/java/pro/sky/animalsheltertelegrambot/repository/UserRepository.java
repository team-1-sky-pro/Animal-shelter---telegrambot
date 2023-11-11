package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalsheltertelegrambot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByIdAndEmailIsNotNullAndPhoneIsNotNull(Long id);
    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phoneNumber);
}

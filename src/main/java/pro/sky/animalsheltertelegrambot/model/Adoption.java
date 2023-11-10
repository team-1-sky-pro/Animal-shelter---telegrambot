package pro.sky.animalsheltertelegrambot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "adoptions")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Адрес электронной почты, связанный с данным принятием на усыновление.
     * Должен быть действительным адресом электронной почты и уникальным для каждой записи Adoption.
     */
    private LocalDate adoptionDate;

    @Column(name = "trial_end_date")
    private LocalDateTime trialEndDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "user_id")
    private Long userId;
}

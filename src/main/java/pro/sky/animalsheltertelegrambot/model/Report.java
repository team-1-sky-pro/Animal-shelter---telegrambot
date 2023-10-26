package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Report сущность
 * @author Rnd-mi
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "reports")
public class Report {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Дата и время, когда был отправлен отчет
     */
    @Column(name = "report_date_time")
    private LocalDateTime dateTime;

    @Column(name = "report_text")
    private String reportText;

    /**
     * Внешний ключ: ID питомца, к которому относится данный отчет
     * @see Pet
     */
    private Long petId;
}

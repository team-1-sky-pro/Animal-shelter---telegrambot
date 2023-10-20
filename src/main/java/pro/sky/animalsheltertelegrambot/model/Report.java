package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Report сущность
 * @author Rnd-mi
 */
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
    private Date dateTime;

    private String reportText;

    /**
     * Внешний ключ: ID питомца, к которому относится данный отчет
     * @see Pet
     */
    private Long petId;
}

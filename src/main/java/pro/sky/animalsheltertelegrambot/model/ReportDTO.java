package pro.sky.animalsheltertelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;
    private String reportText;
    private List<String> photoIds;
}
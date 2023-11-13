package pro.sky.animalsheltertelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalsheltertelegrambot.model.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
}

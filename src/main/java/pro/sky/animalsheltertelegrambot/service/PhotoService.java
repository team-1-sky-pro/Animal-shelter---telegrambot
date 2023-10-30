package pro.sky.animalsheltertelegrambot.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {

    void addPhotosForPet(Long petId, MultipartFile[] photos) throws IOException;

    void addPhotosForReport(Long reportId, MultipartFile[] photos) throws IOException;

    void getPhotosByPetId(Long petId, HttpServletResponse response);

    void getPhotosByReportId(Long reportId, HttpServletResponse response);

    void deletePhotosByPetId(Long petId);

    void deletePhotosByReportId(Long reportId);
}

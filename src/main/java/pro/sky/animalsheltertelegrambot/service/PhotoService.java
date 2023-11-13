package pro.sky.animalsheltertelegrambot.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalsheltertelegrambot.model.Photo;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    void addPhotosForPet(Long petId, MultipartFile[] photos) throws IOException;

    void addPhotosForReport(Long reportId, MultipartFile[] photos) throws IOException;

    void getPhotosByPetId(Long petId, HttpServletResponse response) throws IOException;

    void getPhotosByReportId(Long reportId, HttpServletResponse response) throws IOException;

    void deletePhotosByPetId(Long petId);

    void deletePhotosByReportId(Long reportId);

    void addPhotoForReport(Photo photo);

    List<Photo> findPhotosByReportId(Long reportId);

    void validatePhotos(MultipartFile[] photos);

    int findIndexOfLastPhoto(String filePath);
}

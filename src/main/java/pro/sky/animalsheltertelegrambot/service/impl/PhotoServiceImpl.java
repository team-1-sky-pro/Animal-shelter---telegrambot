package pro.sky.animalsheltertelegrambot.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalsheltertelegrambot.exception.photos.BadPhotoExtensionException;
import pro.sky.animalsheltertelegrambot.exception.photos.LimitOfPhotosException;
import pro.sky.animalsheltertelegrambot.exception.photos.PhotoIsEmptyException;
import pro.sky.animalsheltertelegrambot.exception.photos.PhotoNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.repository.PhotoRepository;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.service.ReportService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static pro.sky.animalsheltertelegrambot.utils.MethodNameRetriever.getMethodName;


@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final PetService petService;
    private final ReportService reportService;

    @Value("${photos.dir.path}")
    private String photosDir;

    @Value("${photos.extensions}")
    private String extensions;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    private Long maxSize;

    @PostConstruct
    public void init() {
        maxSize = DataSize.parse(maxSizeString, DataUnit.BYTES).toBytes();
    }


    @Override
    public void addPhotosForPet(Long petId, MultipartFile[] photos) throws IOException {
        logWhenMethodInvoked(getMethodName());
        log.info("Вызов валидации фото");
        validatePhotos(photos);

        Pet pet = petService.getPet(petId);

        for (MultipartFile photoFile : photos) {
            if (photoFile.getSize() > maxSize) {
                throw new RemoteException("Слишком большая фотография");
            }

            BufferedImage originalImage = ImageIO.read(photoFile.getInputStream());
            if (originalImage == null) {
                throw new IOException("Не удалось прочитать изображение из файла " + photoFile.getOriginalFilename());
            }

            double ratio = Math.min(1024.0 / originalImage.getWidth(), 1024.0 / originalImage.getHeight());
            BufferedImage resizedImage = getScaledInstance(originalImage, ratio);
            String extension = getExtension(photoFile.getOriginalFilename());
            String fileName = "photo_" + petId + "_" + System.currentTimeMillis() + "." + extension; // Избегаем конфликтов имен
            Path resizedFilePath = Paths.get(photosDir, fileName);

            File outputFile = new File(resizedFilePath.toUri());
            ImageIO.write(resizedImage, extension, outputFile);

            Photo photo = new Photo();
            photo.setPet(pet);
            photo.setFilePath(resizedFilePath.toString());
            photo.setFileSize(outputFile.length());
            photo.setMediaType(photoFile.getContentType());

            photoRepository.save(photo); // Сохраняем фото и получаем ID
            pet.setPhoto(photo); // Устанавливаем фото питомцу
            petService.save(pet);
        }
    }


    private BufferedImage getScaledInstance(BufferedImage img, double ratio) {
        int newWidth = (int) (img.getWidth() * ratio);
        int newHeight = (int) (img.getHeight() * ratio);

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    @Override
    public void addPhotosForReport(Long reportId, MultipartFile[] photos) throws IOException {
        logWhenMethodInvoked(getMethodName());
        validatePhotos(photos);
        int start = findIndexOfLastPhoto(photoRepository.findLastFilePathByReportId(reportId).orElse(null));

        for (MultipartFile photoFile : photos) {
            if (++start == 10) {
                throw new LimitOfPhotosException();
            }
            Report report = reportService.getReport(reportId);
            Path filePath = Path.of(photosDir, "number=" + start + "reportId=" + reportId + "." +
                    getExtension(photoFile.getOriginalFilename()));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = photoFile.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            }
            log.debug("Filling avatar object with values and saving in repository");
            Photo photo = new Photo();
            photo.setReport(report);
            photo.setFilePath(filePath.toString());
            photo.setFileSize(photoFile.getSize());
            photo.setMediaType(photoFile.getContentType());
            photoRepository.save(photo);
        }
    }

    @Override
    public void getPhotosByPetId(Long petId, Long photoNumber, HttpServletResponse response) throws IOException {
        logWhenMethodInvoked(getMethodName());
        List<Photo> photos = findPhotosByPetId(petId);
        Photo photo = photos.stream()
                .filter(e -> e.getFilePath().contains("number=" + photoNumber))
                .findAny()
                .orElseThrow(() -> new PhotoNotFoundException());
        try (
                InputStream is = Files.newInputStream(Path.of(photo.getFilePath()));
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream(), 1024)
        ) {
            response.setContentType(photo.getMediaType());
            response.setContentLength(photo.getFileSize().intValue());
            bis.transferTo(bos);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void getPhotosByReportId(Long reportId, Long photoNumber, HttpServletResponse response) throws IOException {
        logWhenMethodInvoked(getMethodName());
        List<Photo> photos = findPhotosByReportId(reportId);
        Photo photo = photos.stream()
                .filter(e -> e.getFilePath().contains("number=" + photoNumber))
                .findAny()
                .orElseThrow(() -> new PhotoNotFoundException());
        try (
                InputStream is = Files.newInputStream(Path.of(photo.getFilePath()));
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream(), 1024)
        ) {
            response.setContentType(photo.getMediaType());
            response.setContentLength(photo.getFileSize().intValue());
            response.setStatus(HttpServletResponse.SC_OK);
            bis.transferTo(bos);
        }
    }

    @Override
    public void deletePhotosByPetId(Long petId) {
        List<Photo> byPetId = photoRepository.findByPetId(petId);
        for (Photo photo : byPetId) {
            photoRepository.delete(photo);
        }
    }

    @Override
    public void deletePhotosByReportId(Long reportId) {
        List<Photo> byReportId = photoRepository.findByReportId(reportId);
        for (Photo photo : byReportId) {
            photoRepository.delete(photo);
        }
    }

    @Override
    public void addPhotoForReport(Photo photo) {
        photoRepository.save(photo);
    }

    /**
     * Метод по поиску хранимых фотографий, относящихся к определенному питомцу
     *
     * @param petId id питомца, фотографии которого требуется найти
     * @return List сущностей Photo
     * @throws PhotoNotFoundException в случае отсутствия фотографий
     */
    private List<Photo> findPhotosByPetId(Long petId) {
        List<Photo> byPetId = photoRepository.findByPetId(petId);
        if (byPetId.isEmpty()) {
            throw new PhotoNotFoundException();
        }
        return byPetId;
    }

    /**
     * Метод по поиску хранимых фотографий, относящихся к определенному отчету
     *
     * @param reportId id отчета, фотографии которого требуется найти
     * @return List сущностей Photo
     * @throws PhotoNotFoundException в случае отсутствия фотографий
     */
    private List<Photo> findPhotosByReportId(Long reportId) {
        List<Photo> byReportId = photoRepository.findByReportId(reportId);
        if (byReportId.isEmpty()) {
            throw new PhotoNotFoundException();
        }
        return byReportId;
    }

    /**
     * Вспомогательный метод, который позволяет вычленить расширение из имени файла
     *
     * @param fileName файл, расширение которого нужно получить
     * @return строка, которая представляет расширение переданного файла
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Вспомогательный метод, который валидирует переданные пользователем файлы.
     * Выбрасывает исключение, если одно из условий срабатывает.
     *
     * @param photos набор файлов, которые нужно проверить
     * @throws PhotoIsEmptyException      если один из файлов не был отправлен
     * @throws BadPhotoExtensionException если один из файлов недопустимого расширения
     */
    private void validatePhotos(MultipartFile[] photos) {
        logWhenMethodInvoked(getMethodName());
        for (MultipartFile photo : photos) {
            if (photo == null) {
                throw new PhotoIsEmptyException();
            }
            if (!(extensions.contains(getExtension(photo.getOriginalFilename())))) {
                throw new BadPhotoExtensionException();
            }
        }
    }

    /**
     * Поиск и возврат порядкового номера последней фотографии.
     * В случае, если полученный список пуст, это значит, что фотографий еще не было добавлено.
     * Следовательно, возвращаем первый номер - 0.
     *
     * @param filePath путь последней добавленной фотографии для питомца/отчета
     * @return номер текущей по счету фотографии или 0, если список пуст
     * @throws LimitOfPhotosException если превышен лимит по количеству фотографий
     */
    private int findIndexOfLastPhoto(String filePath) {
        logWhenMethodInvoked(getMethodName());
        if (filePath == null) {
            return 0;
        } else {
            int curValue = Integer.parseInt(Character.toString(filePath.charAt(filePath.indexOf("=") + 1)));
            if (curValue == 9) {
                throw new LimitOfPhotosException();
            }
            return curValue;
        }
    }

    private void logWhenMethodInvoked(String methodName) {
        log.info("Method '{}'", methodName);
    }
}

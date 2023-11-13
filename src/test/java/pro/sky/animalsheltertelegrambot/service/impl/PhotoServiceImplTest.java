package pro.sky.animalsheltertelegrambot.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalsheltertelegrambot.exception.photos.BadPhotoExtensionException;
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
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private PetService petService;
    @Mock
    private ReportService reportService;
    @InjectMocks
    private PhotoServiceImpl photoService;
    @Mock
    HttpServletResponse response;


    private MultipartFile[] photos;
    private Pet mockPet;
    private Report mockReport;

    @Value("${spring.servlet.multipart.max-file-size}")
    Long MAX_SIZE = 3000L;

    @BeforeEach
    void setUp() throws IOException {
        mockPet = new Pet();
        mockReport = new Report();

        photoService.setExtensions("jpg,png");
        //для тестов нужно создать папку для тестовых фото, например: D:\test\photos
        photoService.setPhotosDir("/test/photos");
        photoService.setMaxSizeString("1048576");
        photoService.init();

    }

    @Test
    void addPhotosForPet_ValidData_Success() throws IOException {
        //проверяем что загрузка фотографии происходит правильно по указанному адресу
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        byte[] buffer = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);

        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getSize()).thenReturn((long) buffer.length);
        when(mockFile.getInputStream()).thenReturn(is);


        photos = new MultipartFile[]{mockFile};

        when(petService.getPet(anyLong())).thenReturn(mockPet);

        photoService.addPhotosForPet(1L, photos);

        verify(petService).getPet(anyLong());
        verify(photoRepository, times(photos.length)).save(any(Photo.class));
        verify(petService).save(any(Pet.class));
    }

    @Test
    void addPhotosForReport_ValidData_Success() throws IOException {
        //проверяем добавление валидной фотографии
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        byte[] buffer = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);

        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getSize()).thenReturn((long) buffer.length);
        when(mockFile.getInputStream()).thenReturn(is);


        photos = new MultipartFile[]{mockFile};

        when(reportService.getReport(anyLong())).thenReturn(mockReport);
        when(photoRepository.findLastFilePathByReportId(anyLong())).thenReturn(Optional.empty());

        photoService.addPhotosForReport(1L, photos);

        verify(reportService).getReport(anyLong());
        verify(photoRepository, times(photos.length)).save(any(Photo.class));
    }

    //как я не пытался, у меня не хватает мозгов написать этот тест :)))
//    @Test
//    void addPhotosForReport_ExceedsLimit_ThrowsLimitOfPhotosException() throws IOException {
//        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        ImageIO.write(image, "jpg", os);
//        byte[] buffer = os.toByteArray();
//
//        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
//        when(mockFile.getSize()).thenReturn((long) buffer.length);
//        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(buffer));
//
//        // Здесь мы настраиваем массив фотографий так, чтобы он содержал 10 элементов
//        photos = new MultipartFile[10];
//        Arrays.fill(photos, mockFile);
//
//        when(reportService.getReport(anyLong())).thenReturn(mockReport);
//        when(photoRepository.findLastFilePathByReportId(anyLong())).thenReturn(Optional.of("number=9"));
//
//        assertThrows(LimitOfPhotosException.class, () -> {
//            photoService.addPhotosForReport(1L, photos);
//        });
//
//        // Проверяем, что методы reportService и photoRepository не вызывались
//        verify(reportService, never()).getReport(anyLong());
//        verify(photoRepository, never()).save(any(Photo.class));
//    }

    @Test
    void getPhotosByPetId_NoPhotos_ThrowsException() {

        PhotoRepository photoRepository = mock(PhotoRepository.class);
        when(photoRepository.findByPetId(anyLong())).thenReturn(List.of());

        PhotoServiceImpl service = new PhotoServiceImpl(photoRepository, null, null);

        assertThrows(PhotoNotFoundException.class, () -> {
            service.getPhotosByPetId(1L, new MockHttpServletResponse());
        });
    }

    @Test
    void getPhotosByPetId_ValidPhotos_Success() throws IOException {
        // Создаем список с фотографией для теста
        List<Photo> photos = new ArrayList<>();
        Photo mockPhoto = new Photo();
        mockPhoto.setFilePath("/test/photos/test.jpg");
        mockPhoto.setFileSize(1024L);
        mockPhoto.setMediaType("image/jpeg");
        photos.add(mockPhoto);

        // Настроим мок PhotoRepository для возврата списка фотографий при вызове findByPetId
        when(photoRepository.findByPetId(anyLong())).thenReturn(photos);

        // Создаем MockHttpServletResponse
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Вызываем метод getPhotosByPetId
        photoService.getPhotosByPetId(1L, response);

        // Проверяем, что ContentType, ContentLength и Status установлены правильно
        assertEquals("image/jpeg", response.getContentType());
        assertEquals(1024, response.getContentLength());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());

    }

    @Test
    void getPhotosByReportId_SuccessfulResponse() throws IOException {
        List<Photo> photos = new ArrayList<>();
        Photo mockPhoto = new Photo();
        mockPhoto.setFilePath("/test/photos/test.jpg");
        mockPhoto.setFileSize(1024L);
        mockPhoto.setMediaType("image/jpeg");
        photos.add(mockPhoto);

        when(photoRepository.findByReportId(anyLong())).thenReturn(photos);

        MockHttpServletResponse response = new MockHttpServletResponse();
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newInputStream(any(Path.class)))
                    .thenReturn(new ByteArrayInputStream(new byte[1024]));

            // Вызовите метод getPhotosByReportId и проверьте, что он не выбрасывает исключение
            assertDoesNotThrow(() -> photoService.getPhotosByReportId(1L, response));

            // Проверьте, что методы response были вызваны правильно
            assertEquals("image/jpeg", response.getContentType());
            assertEquals(1024, response.getContentLength());
            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        }
    }

    @Test
    void getPhotosByReportId_ThrowsPhotoNotFoundException() {

        when(photoRepository.findByReportId(anyLong())).thenReturn(Collections.emptyList());

        // Проверьте, что метод выбрасывает исключение PhotoNotFoundException
        assertThrows(PhotoNotFoundException.class, () -> photoService.getPhotosByReportId(1L, response));
    }

    @Test
    public void testDeletePhotosByPetId() {
        List<Photo> photosToDelete = new ArrayList<>();

        Long petIdToDelete = 1L;

        photoService.deletePhotosByPetId(petIdToDelete);

        for (Photo photo : photosToDelete) {
            verify(photoRepository).delete(photo);
        }
    }

    @Test
    public void testDeletePhotosByReportId() {
        List<Photo> photosToDelete = new ArrayList<>();
        Long reportId = 1L;

        photoService.deletePhotosByReportId(reportId);

        for (Photo photo : photosToDelete) {
            verify(photoRepository).delete(photo);
        }
    }

    @Test
    public void testAddPhotoForReport() {
        Photo testPhoto = new Photo();
        photoService.addPhotoForReport(testPhoto);
        verify(photoRepository).save(testPhoto);
    }

    @Test
    public void testValidatePhotos_WhenPhotosArrayIsNull_ShouldThrowIllegalArgumentException() {
        MultipartFile[] photos = null;
        assertThrows(IllegalArgumentException.class, () -> {
            photoService.validatePhotos(photos);
        });
    }

    @Test
    public void testValidatePhotos_WhenPhotoIsEmpty_ShouldThrowPhotoIsEmptyException() throws IOException {
        // Создаем массив MultipartFile с одним элементом, равным null
        MultipartFile[] photos = new MultipartFile[]{null};

        // Проверяем, что вызов метода validatePhotos с пустым фото вызывает PhotoIsEmptyException
        assertThrows(PhotoIsEmptyException.class, () -> {
            photoService.validatePhotos(photos);
        });
    }

    @Test
    public void testValidatePhotos_WhenPhotoHasBadExtension_ShouldThrowBadPhotoExtensionException() throws IOException {
        // Создаем мок для MultipartFile
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        // Мокируем метод getOriginalFilename, чтобы он возвращал файл с неправильным расширением (например, "file.exe")
        when(mockFile.getOriginalFilename()).thenReturn("file.exe");

        // Создаем массив MultipartFile с одним мокированным файлом
        MultipartFile[] photosWithBadExtension = new MultipartFile[]{mockFile};

        // Проверяем, что вызов метода validatePhotos с фото с неправильным расширением вызывает BadPhotoExtensionException
        assertThrows(BadPhotoExtensionException.class, () -> {
            photoService.validatePhotos(photosWithBadExtension);
        });
    }
}

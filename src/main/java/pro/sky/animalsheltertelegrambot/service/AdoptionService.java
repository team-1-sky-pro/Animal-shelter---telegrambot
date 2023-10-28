package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Adoption;

public interface AdoptionService {

    Adoption addAdoption(Adoption adoption);

    Adoption getAdoption(Long id);

    Adoption updateAdoption(Long id, Adoption adoption);

    void deleteAdoption(Long id);
}

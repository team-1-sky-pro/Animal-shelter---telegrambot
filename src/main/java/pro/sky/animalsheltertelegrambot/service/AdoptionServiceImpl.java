package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Adoption;


@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements  AdoptionService{


    @Override
    public Adoption addAdoption(Adoption adoption) {
        return null;
    }

    @Override
    public Adoption getAdoption(Long id) {
        return null;
    }

    @Override
    public Adoption updateAdoption(Long id, Adoption adoption) {
        return null;
    }

    @Override
    public void deleteAdoption(Long id) {

    }
}

package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Shelter;

import java.util.Collection;

/**
 * реализация сервиса для Приютов
 * базовые CRUD операции + показ всех приютов
 * @ author SyutinS
 */

@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService{

    @Override
    public Shelter addShelter(Shelter shelter) {
        return null;
    }

    @Override
    public Shelter getShelter(Long id) {
        return null;
    }

    @Override
    public Shelter updateShelter(Long id, Shelter shelter) {
        return null;
    }

    @Override
    public void deleteShelter(Long id) {

    }

    @Override
    public Collection<Shelter> getAllShelters() {
        return null;
    }
}

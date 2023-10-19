package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * реализация сервиса для Приютов
 * базовые CRUD операции
 * + показ всех приютов
 * @ author SyutinS
 */

@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService{

    private final Logger logger = LoggerFactory.getLogger(ShelterServiceImpl.class);
    ShelterRepository shelterRepository;

    @Override
    public Shelter addShelter(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public Optional<Shelter> getShelter(Long id) {
        return shelterRepository.findById(id);
    }

    @Override
    public Shelter updateShelter(Long id, Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public void deleteShelter(Long id) {
        logger.info("Was invoked method for deleteShelter by id {} - ", id);
        if (shelterRepository.findById(id).isEmpty()) {
            logger.error("There is no shelter with id - " + id);
// что то забыл. - вспомнить дописать
        }
        shelterRepository.deleteById(id);
    }

    @Override
    public Collection<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }
}

package followarcane.wowdatacrawler.domain.service;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.repository.CharacterInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterInfoService {

    private final CharacterInfoRepository characterInfoRepository;

    public CharacterInfoService(CharacterInfoRepository repository) {
        this.characterInfoRepository = repository;
    }

    public List<CharacterInfo> getAllCharacterInfos() {
        return characterInfoRepository.findAll();
    }

    public CharacterInfo createCharacterInfo(CharacterInfo characterInfo) {
        return characterInfoRepository.save(characterInfo);
    }

    // more methods as needed for your business logic
}
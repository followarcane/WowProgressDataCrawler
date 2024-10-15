package followarcane.wowdatacrawler.infrastructure.service.repoService;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.infrastructure.repository.CharacterInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CharacterInfoService {

    private final CharacterInfoRepository characterInfoRepository;

    public List<CharacterInfo> getAllCharacterInfos() {
        return characterInfoRepository.findAll();
    }

    public void saveAll(List<CharacterInfo> list) {
        characterInfoRepository.saveAll(list);
    }

    public void deleteAll() {
        characterInfoRepository.deleteAll();
    }
}
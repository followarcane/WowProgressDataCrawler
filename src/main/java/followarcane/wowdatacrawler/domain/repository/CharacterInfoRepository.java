package followarcane.wowdatacrawler.domain.repository;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import followarcane.wowdatacrawler.domain.model.CharacterInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfoDTO, Long> {
    CharacterInfo findByName(String charName);
}

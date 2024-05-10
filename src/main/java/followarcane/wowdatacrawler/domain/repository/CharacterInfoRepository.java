package followarcane.wowdatacrawler.domain.repository;

import followarcane.wowdatacrawler.domain.model.CharacterInfoResponse;
import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, Long> {
    CharacterInfoResponse findByName(String charName);
}

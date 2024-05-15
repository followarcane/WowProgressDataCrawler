package followarcane.wowdatacrawler.infrastructure.repository;

import followarcane.wowdatacrawler.domain.model.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, Long> {
    CharacterInfo findByName(String charName);

}

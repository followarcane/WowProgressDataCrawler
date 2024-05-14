package followarcane.wowdatacrawler.domain.repository;

import followarcane.wowdatacrawler.domain.model.RaidProgression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaidProgressionRepository extends JpaRepository<RaidProgression, Long> {

}

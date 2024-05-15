package followarcane.wowdatacrawler.infrastructure.repository;

import followarcane.wowdatacrawler.domain.model.RaiderIOData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaiderIODataRepository extends JpaRepository<RaiderIOData, Long> {
}

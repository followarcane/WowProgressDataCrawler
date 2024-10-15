package followarcane.wowdatacrawler.infrastructure.repository;

import followarcane.wowdatacrawler.domain.model.WarcraftLogsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarcraftLogsRepository extends JpaRepository<WarcraftLogsData, Long> {
    default Optional<String> authenticate(String clientId, String clientSecret) {
        return Optional.empty();
    }
}

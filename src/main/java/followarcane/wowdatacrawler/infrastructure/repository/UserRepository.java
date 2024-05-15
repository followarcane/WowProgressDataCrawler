package followarcane.wowdatacrawler.infrastructure.repository;

import followarcane.wowdatacrawler.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}

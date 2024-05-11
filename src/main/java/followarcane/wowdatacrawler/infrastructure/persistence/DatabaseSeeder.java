package followarcane.wowdatacrawler.infrastructure.persistence;

import followarcane.wowdatacrawler.domain.model.User;
import followarcane.wowdatacrawler.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {
        if(userRepository.count() == 0){
            User user = new User();
            user.setUsername("pyro");
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("pyro"));
            user.setRole("ROLE_FAKE_ADMIN");

            userRepository.save(user);
        }
    }
}

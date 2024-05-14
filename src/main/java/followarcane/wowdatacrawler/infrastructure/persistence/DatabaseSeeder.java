package followarcane.wowdatacrawler.infrastructure.persistence;

import followarcane.wowdatacrawler.domain.model.User;
import followarcane.wowdatacrawler.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {
        if(userRepository.count() == 0){
            User user = new User();
            user.setUsername("pyro");
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("pyro"));
            user.setRole("FAKE_ADMIN");

            userRepository.save(user);
        }
    }
}

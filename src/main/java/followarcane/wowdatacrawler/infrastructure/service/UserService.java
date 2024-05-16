package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.api.v1.Requests.CreateUserCommand;
import followarcane.wowdatacrawler.api.v1.Requests.UpdateUserCommand;
import followarcane.wowdatacrawler.domain.model.User;
import followarcane.wowdatacrawler.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(CreateUserCommand createUserCommand) {
        return userRepository.save(User.builder()
                .username(createUserCommand.getUsername())
                .password(passwordEncoder.encode(createUserCommand.getPassword()))
                .role(createUserCommand.getRole())
                .enabled(true)
                .build());
    }

    public User updateUser(Long id, UpdateUserCommand updateUserCommand) {
        User user = getUser(id);
        user.setUsername(updateUserCommand.getUsername());
        user.setRole(updateUserCommand.getRole());
        user.setEmail(updateUserCommand.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        userRepository.save(user);
    }


    private User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }
}
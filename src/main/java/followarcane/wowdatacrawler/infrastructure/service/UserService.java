package followarcane.wowdatacrawler.infrastructure.service;

import followarcane.wowdatacrawler.api.v1.error.UserAlreadyExistsException;
import followarcane.wowdatacrawler.api.v1.error.UserNotFoundException;
import followarcane.wowdatacrawler.api.v1.requests.CreateUserCommand;
import followarcane.wowdatacrawler.api.v1.requests.UpdateUserCommand;
import followarcane.wowdatacrawler.domain.model.User;
import followarcane.wowdatacrawler.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        User user = userRepository.findByUsername(createUserCommand.getUsername());
        if (Objects.nonNull(user)) {
            throw new UserAlreadyExistsException();
        }

        return userRepository.save(User.builder()
                .username(createUserCommand.getUsername())
                .password(passwordEncoder.encode(createUserCommand.getPassword()))
                .email(createUserCommand.getEmail())
                .role(createUserCommand.getRole().name())
                .enabled(true)
                .build());
    }

    public User updateUser(Long id, UpdateUserCommand updateUserCommand) {
        User user = getUser(id);
        user.setUsername(updateUserCommand.getUsername());
        user.setRole(updateUserCommand.getRole().name());
        user.setEmail(updateUserCommand.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        user.setEnabled(false);
        userRepository.save(user);
    }


    private User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }
}
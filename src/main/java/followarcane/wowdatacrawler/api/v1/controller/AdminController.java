package followarcane.wowdatacrawler.api.v1.controller;

import followarcane.wowdatacrawler.api.v1.responses.UserResponse;
import followarcane.wowdatacrawler.api.v1.requests.CreateUserCommand;
import followarcane.wowdatacrawler.api.v1.requests.UpdateUserCommand;
import followarcane.wowdatacrawler.domain.converter.ResponseConverter;
import followarcane.wowdatacrawler.domain.model.User;
import followarcane.wowdatacrawler.infrastructure.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
@RequestMapping("/api/v1/admin/users")
public class AdminController {

    private final UserService userService;
    private final ResponseConverter responseConverter;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(responseConverter.convertUser(users));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserCommand createUserCommand) {
        User user = userService.createUser(createUserCommand);
        return ResponseEntity.ok(responseConverter.convertUser(user));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserCommand updateUserCommand) {
        User user = userService.updateUser(id, updateUserCommand);
        return ResponseEntity.ok(responseConverter.convertUser(user));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

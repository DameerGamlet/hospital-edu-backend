package edu.medical.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.medical.demo.model.User;
import edu.medical.demo.model.dto.request.CreateUserRequest;
import edu.medical.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public UUID createUser(@RequestBody CreateUserRequest request) throws JsonProcessingException {
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    public User getUserByUserId(@PathVariable UUID userId) {
        return userService.getUserByUserId(userId);
    }
}

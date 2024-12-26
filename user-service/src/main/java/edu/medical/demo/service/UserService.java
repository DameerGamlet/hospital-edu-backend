package edu.medical.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.medical.demo.model.User;
import edu.medical.demo.model.dto.request.CreateUserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserByUserId(UUID userId);
    UUID createUser(CreateUserRequest request) throws JsonProcessingException;
    List<User> getUsers();
}

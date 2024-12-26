package edu.medical.demo.controller.private_api;

import edu.medical.demo.service.UserActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("private/api/v1/users")
public class UserActivationController {

    private final UserActivationService userActivationService;

    @GetMapping("/{userId}/activation")
    public void activationCode(@PathVariable UUID userId, @RequestParam() String code) {
        userActivationService.registrationUserRegister(userId, code);
    }
}

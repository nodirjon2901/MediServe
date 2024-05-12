package org.example.mediserve.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.dto.request.UserCreateDTO;
import org.example.mediserve.domain.dto.request.UserLoginDTO;
import org.example.mediserve.domain.dto.response.UserResponseDTO;
import org.example.mediserve.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            description = "This API is used for user sing-up",
            method = "Post method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"Any role"})
    )
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserCreateDTO userCreateDTO) {
        userService.signUp(userCreateDTO);
        return ResponseEntity.status(HttpStatus.valueOf(201)).build();
    }

    @Operation(
            description = "This API is used for user sing-in",
            method = "Post method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"Any role"})
    )
    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDTO> signIn(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.signIn(userLoginDTO));
    }

}

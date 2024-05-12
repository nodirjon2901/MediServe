package org.example.mediserve.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.dto.request.UserCreateDTO;
import org.example.mediserve.domain.dto.response.UserResponseDTO;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            description = "This API is used to find all users",
            method = "Get method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN"})
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(
            description = "This API is used to find a user by ID",
            method = "GET method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN", "USER"})
    )
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/find/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            description = "This API is used to update a user by ID",
            method = "PUT method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN"})
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody UserCreateDTO userCreateDTO
    ) {
        return ResponseEntity.ok(userService.update(userCreateDTO, id));
    }

    @Operation(
            description = "This API is used to delete a user by ID",
            method = "DELETE method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN"})
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.valueOf(204)).build();
    }

    @Operation(
            description = "This API is used to change the role of a user by ID",
            method = "PATCH method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN"})
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changeRole/{id}")
    public ResponseEntity<UserResponseDTO> changeRole(
            @PathVariable UUID id,
            @RequestParam UserRole role
    ) {
        return ResponseEntity.ok(userService.changeRole(id, role));
    }

}

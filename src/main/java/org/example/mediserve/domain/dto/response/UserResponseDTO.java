package org.example.mediserve.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.domain.enums.UserState;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {

    protected UUID id;

    private Long chatId;

    private String firstName;

    private String lastName;

    private String experience;

    private String username;

    private String password;

    private String phoneNumber;

    private Boolean isPaid;

    private UserState state;

    private UserRole role;

    protected LocalDateTime createdDate;

    protected LocalDateTime updatedDate;

}

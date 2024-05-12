package org.example.mediserve.domain.dto.request;

import lombok.*;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.domain.enums.UserState;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCreateDTO {

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

}

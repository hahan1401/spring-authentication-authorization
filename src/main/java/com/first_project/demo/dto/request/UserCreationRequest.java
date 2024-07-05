package com.first_project.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String id;
    private String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String firstname;
    private String lastname;
    private LocalDate dob;
    private String role;
}

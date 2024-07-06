package com.first_project.demo.dto.request;

import com.first_project.demo.vadidator.DobConstraint;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @DobConstraint(min = 10, message = "INVALID_DOB")
  private LocalDate dob;

  private Set<String> roles;
}

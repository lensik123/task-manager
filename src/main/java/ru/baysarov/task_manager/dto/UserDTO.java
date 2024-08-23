package ru.baysarov.task_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  @NotEmpty(message = "First name cannot be empty")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  private String firstname;


  @NotEmpty(message = "Last name cannot be empty")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  private String lastname;

  @NotEmpty(message = "Email cannot be empty")
  @Email(message = "Email should be valid")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=-]).*$",
      message = "Password must contain at least one digit, one letter, and one special character (@#$%^&+=-)"
  )
  private String password;
}

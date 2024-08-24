package ru.baysarov.task_manager.validators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.UserRepository;

@SpringBootTest
public class UserValidatorTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserValidator userValidator;

  @Test
  public void whenValidate_thenErrorsIfEmailExists() {
    User user = User.builder()
        .email("existing.email@example.com")
        .build();
    when(userRepository.findByEmail("existing.email@example.com")).thenReturn(Optional.of(user));

    Errors errors = mock(Errors.class);
    userValidator.validate(user, errors);

    verify(errors).rejectValue("email", null, "This email address is already in use");
  }
}

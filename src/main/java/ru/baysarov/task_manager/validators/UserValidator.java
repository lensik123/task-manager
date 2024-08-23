package ru.baysarov.task_manager.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.UserRepository;


@Component
public class UserValidator implements Validator {

  private final UserRepository userRepository;

  @Autowired
  public UserValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    User user = (User) target;

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      errors.rejectValue("email", null, "This email address is already in use");
    }
  }
}

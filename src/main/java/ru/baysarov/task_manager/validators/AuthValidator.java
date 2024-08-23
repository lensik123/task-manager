package ru.baysarov.task_manager.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AuthValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return false;
  }

  @Override
  public void validate(Object target, Errors errors) {

  }
}

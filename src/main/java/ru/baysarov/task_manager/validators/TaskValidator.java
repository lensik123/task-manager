package ru.baysarov.task_manager.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.baysarov.task_manager.models.Task;

@Component
public class TaskValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Task.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

  }

}
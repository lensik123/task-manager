package ru.baysarov.task_manager.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.models.Task;

@Component
public class TaskValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Task.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Task task = (Task) target;

    // Проверка допустимости значения TaskStatus
    if (task.getStatus() == null) {
      errors.rejectValue("status", "task.status.null", "Task status must not be null");
    } else if (!isValidEnumValue(TaskStatus.class, task.getStatus().name())) {
      errors.rejectValue("status", "task.status.invalid", "Invalid task status value. Should be WAITING, IN_PROCESS or DONE");
    }

    // Проверка допустимости значения TaskPriority
    if (task.getPriority() == null) {
      errors.rejectValue("priority", "task.priority.null", "Task priority must not be null");
    } else if (!isValidEnumValue(TaskPriority.class, task.getPriority().name())) {
      errors.rejectValue("priority", "task.priority.invalid", "Invalid task priority value. Should be HIGH, MEDIUM or LOW");
    }

  }

  // Вспомогательный метод для проверки допустимости значения перечисления
  private <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String value) {
    for (E enumValue : enumClass.getEnumConstants()) {
      if (enumValue.name().equals(value)) {
        return true;
      }
    }
    return false;
  }
}
package ru.baysarov.task_manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.baysarov.task_manager.exceptions.InvalidTaskStatusException;
import ru.baysarov.task_manager.util.EnumUtils;

public enum TaskPriority {
  HIGH,
  MEDIUM,
  LOW;


  @JsonCreator
  public static TaskPriority fromString(String value) {
    try {
      return TaskPriority.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      String values = EnumUtils.getEnumValues(TaskPriority.class);
      throw new InvalidTaskStatusException("\nInvalid task priority: " + value + ". \nMust be one of: " + values);
    }
  }

  @JsonValue
  public String toJson() {
    return name();
  }
}

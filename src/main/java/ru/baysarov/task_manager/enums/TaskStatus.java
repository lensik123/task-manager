package ru.baysarov.task_manager.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ru.baysarov.task_manager.exceptions.InvalidTaskStatusException;
import ru.baysarov.task_manager.util.EnumUtils;

public enum TaskStatus {
  WAITING,
  IN_PROCESS,
  DONE;

  @JsonCreator
  public static TaskStatus fromString(String value) {
    try {
      return TaskStatus.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      String values = EnumUtils.getEnumValues(TaskStatus.class);
      throw new InvalidTaskStatusException("\nInvalid task status: " + value + ". \nMust be one of: " + values);
    }
  }

  @JsonValue
  public String toJson() {
    return name();
  }
}

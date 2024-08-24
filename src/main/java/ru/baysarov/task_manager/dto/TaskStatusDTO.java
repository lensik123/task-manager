package ru.baysarov.task_manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.baysarov.task_manager.enums.TaskStatus;

@Data
public class TaskStatusDTO {

  @NotNull(message = "Task status cannot be null")
  private TaskStatus taskStatus;
}

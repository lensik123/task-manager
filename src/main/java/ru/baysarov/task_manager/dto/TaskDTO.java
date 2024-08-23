package ru.baysarov.task_manager.dto;

import lombok.Data;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;

@Data
public class TaskDTO {

  private String taskName;
  private String description;
  private TaskStatus status ;
  private TaskPriority priority;
  private String assignee ;
}
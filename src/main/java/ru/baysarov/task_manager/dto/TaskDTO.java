package ru.baysarov.task_manager.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;

@Data
public class TaskDTO {

  @NotBlank(message = "Task name is required")
  @Size(max = 100, message = "Task name cannot exceed 100 characters")
  @Column(name = "task_name")
  private String taskName;

  @Size(max = 500, message = "Description cannot exceed 500 characters")
  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TaskStatus status;

  @NotNull(message = "Task priority is required")
  @Enumerated(EnumType.STRING)
  @Column(name = "priority")
  private TaskPriority priority;

}
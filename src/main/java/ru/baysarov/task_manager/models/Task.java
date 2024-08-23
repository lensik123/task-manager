package ru.baysarov.task_manager.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;

@Entity
@Data
@Table(name = "task")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotBlank(message = "Task name is required")
  @Size(max = 100, message = "Task name cannot exceed 100 characters")
  @Column(name = "task_name")
  private String taskName;

  @Size(max = 500, message = "Description cannot exceed 500 characters")
  @Column(name = "description")
  private String description;

  @NotNull(message = "Task status is required")
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TaskStatus status;

  @NotNull(message = "Task priority is required")
  @Enumerated(EnumType.STRING)
  @Column(name = "priority")
  private TaskPriority priority;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "assignee_id", referencedColumnName = "id")
  private User assignee;
}
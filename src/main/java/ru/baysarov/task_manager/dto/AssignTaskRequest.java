package ru.baysarov.task_manager.dto;

public class AssignTaskRequest {
  private Integer assigneeId;

  // Геттеры и сеттеры
  public Integer getAssigneeId() {
    return assigneeId;
  }

  public void setAssigneeId(Integer assigneeId) {
    this.assigneeId = assigneeId;
  }
}


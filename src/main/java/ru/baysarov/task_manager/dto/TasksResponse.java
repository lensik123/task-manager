package ru.baysarov.task_manager.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TasksResponse {

  private List<TaskDTO> tasks;
}

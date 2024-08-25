package ru.baysarov.task_manager.controllers;

import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.baysarov.task_manager.dto.TaskDTO;
import ru.baysarov.task_manager.dto.TasksResponse;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.services.TasksService;
import ru.baysarov.task_manager.services.UsersServIce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/personal")
public class PeopleController {

  private final UsersServIce usersServIce;
  private final TasksService tasksService;
  private final ModelMapper modelMapper;

  @Autowired
  public PeopleController(UsersServIce usersServIce, TasksService tasksService,
      ModelMapper modelMapper) {
    this.usersServIce = usersServIce;
    this.tasksService = tasksService;
    this.modelMapper = modelMapper;
  }

  @Operation(summary = "Get tasks assigned to a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "403",description = "Unauthorized / Invalid token")
  })
  @GetMapping("/user/{id}/tasks")
  public TasksResponse getTasks(@PathVariable("id") int assigneeId) {
    return new TasksResponse(tasksService.findAllByAssigneeId(assigneeId)
        .stream()
        .map(this::convertTaskDTO)
        .collect(Collectors.toList()));
  }

  public TaskDTO convertTaskDTO(Task task) {
    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

    if (task.getAssignee() != null) {
      taskDTO.setAssignee(task.getAssignee().getEmail());
    } else {
      taskDTO.setAssignee(null);
    }

    if (task.getAuthor() != null) {
      taskDTO.setAuthor(task.getAuthor().getEmail());
    } else {
      taskDTO.setAuthor(null);
    }

    return taskDTO;
  }

  public Task convertToTask(TaskDTO taskDTO) {
    return modelMapper.map(taskDTO, Task.class);
  }

}

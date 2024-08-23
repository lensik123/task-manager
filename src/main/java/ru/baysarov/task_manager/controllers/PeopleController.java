package ru.baysarov.task_manager.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.baysarov.task_manager.dto.TaskDTO;
import ru.baysarov.task_manager.dto.TasksResponse;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.services.TasksService;
import ru.baysarov.task_manager.services.UsersServIce;

@RestController
@RequestMapping("/api/v1/people")
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

  @GetMapping("/{id}/tasks")
  public TasksResponse getTasks(@PathVariable("id") int assigneeId) {

    return new TasksResponse(tasksService.findAllByAssigneeId(assigneeId)
        .stream()
        .map(this::convertTaskDTO)
        .collect(Collectors.toList()
        )
    );

  }


  public TaskDTO convertTaskDTO(Task task) {
    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
    taskDTO.setAssignee(task.getAssignee().getEmail());
    return taskDTO;
  }
  public Task convertToTask(TaskDTO taskDTO) {
    return modelMapper.map(taskDTO, Task.class);
  }

}

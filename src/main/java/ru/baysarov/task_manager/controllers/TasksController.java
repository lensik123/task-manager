package ru.baysarov.task_manager.controllers;


import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.baysarov.task_manager.dto.TaskDTO;
import ru.baysarov.task_manager.dto.TasksResponse;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.services.TasksService;
import ru.baysarov.task_manager.validators.TaskValidator;


@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {

  private final TasksService tasksService;
  private final TaskValidator taskValidator;
  private final ModelMapper modelMapper;

  @Autowired
  public TasksController(TasksService tasksService, TaskValidator taskValidator,
      ModelMapper modelMapper) {
    this.tasksService = tasksService;
    this.taskValidator = taskValidator;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/{id}")
  public TaskDTO index(@PathVariable int id) {
    return convertTaskDTO(tasksService.findById(id));
  }

  @GetMapping()
  public TasksResponse getAllTasks() {
    return new TasksResponse(
        tasksService.findAll()
            .stream()
            .map(this::convertTaskDTO)
            .collect(Collectors.toList())
    );
  }


  @PostMapping()
  public ResponseEntity<?> createTask(@RequestBody @Valid TaskDTO taskDTO,
      BindingResult bindingResult) {
    ResponseEntity<?> errors = getResponseEntity(bindingResult);
    if (errors != null) {
      return errors;
    }

    tasksService.save(convertToTask(taskDTO));
    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  @PatchMapping("/{id}/assignee")
  public ResponseEntity<?> assignTask(@PathVariable int id,
      @RequestBody Map<String, Integer> updates) {
    tasksService.assignTask(id, updates.get("assigneeId"));
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<HttpStatus> updateStatus(@PathVariable int id,
      @RequestBody Map<String, String> updates) {
    tasksService.updateStatus(id, updates.get("status"));
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable int id,
      @RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
    Task task = convertToTask(taskDTO);
    ResponseEntity<?> errors = getResponseEntity(bindingResult);
    if (errors != null) {
      return errors;
    }
    taskValidator.validate(task, bindingResult);
    tasksService.update(id, task);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTaskById(@PathVariable int id) {
    tasksService.delete(id);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  //Мапперы

  public TaskDTO convertTaskDTO(Task task) {
    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
    return taskDTO;
  }

  public Task convertToTask(TaskDTO taskDTO) {
    return modelMapper.map(taskDTO, Task.class);
  }


  static ResponseEntity<?> getResponseEntity(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      for (FieldError error : bindingResult.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    return null;
  }
}

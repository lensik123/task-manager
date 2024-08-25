package ru.baysarov.task_manager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.baysarov.task_manager.dto.AssignTaskRequest;
import ru.baysarov.task_manager.dto.CommentDTO;
import ru.baysarov.task_manager.dto.CommentsResponse;
import ru.baysarov.task_manager.dto.TaskDTO;
import ru.baysarov.task_manager.dto.TaskStatusDTO;
import ru.baysarov.task_manager.dto.TasksResponse;
import ru.baysarov.task_manager.models.Comment;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.services.CommentsService;
import ru.baysarov.task_manager.services.TasksService;
import ru.baysarov.task_manager.validators.TaskValidator;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {

  private final TasksService tasksService;
  private final CommentsService commentsService;
  private final TaskValidator taskValidator;
  private final ModelMapper modelMapper;

  @Autowired
  public TasksController(TasksService tasksService, CommentsService commentsService,
      TaskValidator taskValidator,
      ModelMapper modelMapper) {
    this.tasksService = tasksService;
    this.commentsService = commentsService;
    this.taskValidator = taskValidator;
    this.modelMapper = modelMapper;
  }

  @Operation(summary = "Get tasks by filters")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @GetMapping()
  public TasksResponse getTasks(
      @RequestParam(required = false) Integer authorId,
      @RequestParam(required = false) Integer assigneeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    List<Task> tasks = tasksService.findTasksByAuthorOrAssignee(authorId, assigneeId, page, size);

    return new TasksResponse(tasks.stream()
        .map(this::convertTaskDTO)
        .collect(Collectors.toList()));
  }

  @Operation(summary = "Get task by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @GetMapping("/{id}")
  public TaskDTO index(@PathVariable int id) {
    return convertTaskDTO(tasksService.findById(id));
  }

  @Operation(summary = "Get comments by task ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @GetMapping("/{id}/comments")
  public CommentsResponse getCommentsByTask(@PathVariable int id) {
    List<Comment> comments = commentsService.findAllById(id);

    return new CommentsResponse(comments
        .stream()
        .map(this::convertToCommentDTO)
        .collect(Collectors.toList()));
  }

  @Operation(summary = "Create a new task")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Task created successfully"),
      @ApiResponse(responseCode = "400", description = "Validation errors"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @PostMapping()
  public ResponseEntity<?> createTask(@RequestBody @Valid TaskDTO taskDTO,
      BindingResult bindingResult) {
    ResponseEntity<?> errors = getResponseEntity(bindingResult);
    if (errors != null) {
      return errors;
    }

    tasksService.save(convertToTask(taskDTO));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Add a comment to a task")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Comment added successfully"),
      @ApiResponse(responseCode = "400", description = "Validation errors"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @PostMapping("/{taskId}/comment")
  public ResponseEntity<?> addComment(@PathVariable int taskId,
      @RequestBody @Valid CommentDTO commentDTO,
      BindingResult bindingResult) {
    ResponseEntity<?> errors = getResponseEntity(bindingResult);
    if (errors != null) {
      return errors;
    }

    commentsService.addComment(taskId, convertToComment(commentDTO));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Assign a task to a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @PatchMapping("/{id}/assignee")
  public ResponseEntity<?> assignTask(@PathVariable int id,
      @RequestBody AssignTaskRequest request) {
    tasksService.assignTask(id, request.getAssigneeId());
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @Operation(summary = "Update task status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
      @ApiResponse(responseCode = "400", description = "Validation errors"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(@PathVariable int id,
      @RequestBody @Valid TaskStatusDTO statusUpdateDTO) {

    tasksService.updateStatus(id, statusUpdateDTO.getTaskStatus());

    return ResponseEntity.ok(HttpStatus.OK);
  }

  @Operation(summary = "Update a task")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task updated successfully"),
      @ApiResponse(responseCode = "400", description = "Validation errors"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @PutMapping("/{id}")
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

  @Operation(summary = "Delete a task by ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTaskById(@PathVariable int id) {
    tasksService.delete(id);
    return ResponseEntity.ok(HttpStatus.OK);
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

  // Мапперы
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

  public CommentDTO convertToCommentDTO(Comment comment) {
    return modelMapper.map(comment, CommentDTO.class);
  }

  public Comment convertToComment(CommentDTO commentDTO) {
    return modelMapper.map(commentDTO, Comment.class);
  }
}

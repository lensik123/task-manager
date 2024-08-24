package ru.baysarov.task_manager.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.exceptions.TaskAccessException;
import ru.baysarov.task_manager.exceptions.TaskNotFoundException;
import ru.baysarov.task_manager.exceptions.UserNotFoundException;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.TaskRepository;
import ru.baysarov.task_manager.repositories.UserRepository;
import ru.baysarov.task_manager.util.SecurityUtils;

@Service
@Transactional
public class TasksService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  @Autowired
  public TasksService(TaskRepository taskRepository, UserRepository userRepository) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
  }

  public List<Task> findAll() {
    return taskRepository.findAll();
  }

  public Task findById(int id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
  }

  @Transactional
  public void save(Task task) {
    // Получаем текущего пользователя
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = authentication.getName();

    // Получаем объект User из базы данных по email
    User author = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));

    task.setAuthor(author);
    task.setStatus(TaskStatus.WAITING);

    taskRepository.save(task);
  }


  @Transactional
  public void delete(int id) {
    taskRepository.deleteById(id);
  }


  @Transactional
  public void updateStatus(int taskId, String status) {

    String userEmail = SecurityUtils.getCurrentUserEmail();

    User currentUser = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));

    Task existingTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

    if (!existingTask.getAssignee().equals(currentUser) && !existingTask.getAuthor()
        .equals(currentUser)) {
      throw new TaskAccessException("You must be the author or assignee of the task to update it.");
    }

    TaskStatus taskStatus;
    try {
      taskStatus = TaskStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid status value: " + status + " Must be WAITING,\n"
          + "  IN_PROCESS,\n"
          + "  or DONE\n");
    }

    existingTask.setStatus(taskStatus);
    taskRepository.save(existingTask);
  }

  @Transactional
  public void assignTask(int taskId, int assigneeId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

    User user = userRepository.findById(assigneeId)
        .orElseThrow(() -> new UserNotFoundException("User " + assigneeId + " not found"));

    task.setAssignee(user);
    taskRepository.save(task);
  }

  public List<Task> findAllByAssigneeId(int assigneeId) {
    return taskRepository.findAllByAssigneeId(assigneeId);
  }

  public void update(int id, Task updatedTask) {
    updatedTask.setId(id);
    taskRepository.save(updatedTask);
  }

  public List<Task> findTasksByAuthorOrAssignee(Integer authorId, Integer assigneeId, int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);

    if (authorId != null && assigneeId != null) {
      return taskRepository.findByAuthorIdOrAssigneeId(authorId, assigneeId, pageable);
    } else if (authorId != null) {
      return taskRepository.findByAuthorId(authorId, pageable);
    } else if (assigneeId != null) {
      return taskRepository.findByAssigneeId(assigneeId, pageable);
    } else {
      return taskRepository.findAll();
    }

  }
}

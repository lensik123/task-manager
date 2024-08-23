package ru.baysarov.task_manager.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.exceptions.TaskNotFoundException;
import ru.baysarov.task_manager.exceptions.UserNotFoundException;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.TasksRepository;
import ru.baysarov.task_manager.repositories.UserRepository;
import ru.baysarov.task_manager.util.SecurityUtils;

@Service
@Transactional
public class TasksService {

  private final TasksRepository tasksRepository;
  private final UserRepository userRepository;

  @Autowired
  public TasksService(TasksRepository tasksRepository, UserRepository userRepository) {
    this.tasksRepository = tasksRepository;
    this.userRepository = userRepository;
  }

  public List<Task> findAll() {
    return tasksRepository.findAll();
  }

  public Task findById(int id) {
    return tasksRepository.findById(id)
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

    tasksRepository.save(task);
  }


  @Transactional
  public void delete(int id) {
    tasksRepository.deleteById(id);
  }


  @Transactional
  public void updateStatus(int taskId, String status) {

    String userEmail = SecurityUtils.getCurrentUserEmail();

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));

    Task existingTask = tasksRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

    if (!existingTask.getAssignee().equals(user)) {
      throw new UserNotFoundException("User " + userEmail + " not found");
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
    tasksRepository.save(existingTask);
  }

  @Transactional
  public void assignTask(int taskId, int assigneeId) {
    Task task = tasksRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));

    User user = userRepository.findById(assigneeId)
        .orElseThrow(() -> new UserNotFoundException("User " + assigneeId + " not found"));

    task.setAssignee(user);
    tasksRepository.save(task);
  }

  public List<Task> findAllByAssigneeId(int assigneeId) {
    return tasksRepository.findAllByAssigneeId(assigneeId);
  }
}

package ru.baysarov.task_manager.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.exceptions.TaskNotFoundException;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.TaskRepository;
import ru.baysarov.task_manager.repositories.UserRepository;

@SpringBootTest

class TasksServiceTest {

  @Autowired
  private TasksService tasksService;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Transactional
  public void whenUpdateStatus_thenSuccess() {
    User user = User.builder()
        .firstname("Alice")
        .lastname("Smith")
        .email("alice.smith@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();
    userRepository.save(user);

    Task task = Task.builder()
        .taskName("Jira")
        .description("Jira task")
        .assignee(user)
        .author(user)
        .status(TaskStatus.WAITING)
        .priority(TaskPriority.HIGH)
        .build();
    taskRepository.save(task);

    System.out.println(user);
    tasksService.updateStatus(task.getId(), TaskStatus.DONE);
    Task updatedTask = taskRepository.findById(task.getId()).orElseThrow(() -> new TaskNotFoundException("Task not found"));

    assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.DONE);
  }

}
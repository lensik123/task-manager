package ru.baysarov.task_manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.TaskRepository;
import ru.baysarov.task_manager.repositories.UserRepository;
import ru.baysarov.task_manager.services.TasksService;
import ru.baysarov.task_manager.util.SecurityUtils;

@SpringJUnitConfig
class TasksServiceTest {

  @InjectMocks
  private TasksService tasksService;

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityUtils securityUtils;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenUpdateStatus_thenSuccess() {
    // Создание и настройка пользователя
    User user = User.builder()
        .id(1)
        .firstname("Alice")
        .lastname("Smith")
        .email("alice.smith@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();

    // Настройка моков
    when(securityUtils.getCurrentUserEmail()).thenReturn("alice.smith@example.com");
    when(userRepository.findByEmail("alice.smith@example.com"))
        .thenReturn(Optional.of(user));

    // Создание и настройка задачи
    Task task = Task.builder()
        .id(1)
        .taskName("Jira")
        .description("Jira task")
        .assignee(user)
        .author(user)
        .status(TaskStatus.WAITING)
        .priority(TaskPriority.HIGH)
        .build();

    when(taskRepository.findById(1)).thenReturn(Optional.of(task));

    // Настройка моков для save
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Обновление статуса задачи
    tasksService.updateStatus(1, TaskStatus.DONE);

    // Создание ArgumentCaptor для захвата аргумента, переданного в метод save
    ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
    verify(taskRepository).save(taskCaptor.capture());

    // Проверка захваченного объекта
    Task savedTask = taskCaptor.getValue();
    assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.DONE);
  }
}

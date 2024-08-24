package ru.baysarov.task_manager.repositories;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.enums.TaskStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void whenFindByAuthorIdOrAssigneeId_thenReturnTasks() {
    User user = User.builder()
        .firstname("Jane")
        .lastname("Doe")
        .email("jane.doe@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();
    userRepository.save(user);

    Task task = Task.builder()
        .author(user)
        .assignee(user)
        .status(TaskStatus.WAITING)
        .priority(TaskPriority.HIGH)
        .taskName("Jira")
        .description("jira description")
        .build();
    taskRepository.save(task);

    List<Task> tasks = taskRepository.findByAuthorIdOrAssigneeId(user.getId(), user.getId(), PageRequest.of(0, 10));
    assertThat(tasks).hasSize(1);
    assertThat(tasks.get(0).getAuthor()).isEqualTo(user);
  }
}

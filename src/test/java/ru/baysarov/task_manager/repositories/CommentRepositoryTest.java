package ru.baysarov.task_manager.repositories;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.enums.TaskPriority;
import ru.baysarov.task_manager.enums.TaskStatus;
import ru.baysarov.task_manager.models.Comment;
import ru.baysarov.task_manager.models.Task;

import java.util.List;
import ru.baysarov.task_manager.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class CommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void whenFindAllByTaskId_thenReturnComments() {

    User user = User.builder()
        .firstname("John")
        .lastname("Doe")
        .email("john.doe@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();

    Task task = new Task();
    task.setTaskName("Sample Task");
    task.setPriority(TaskPriority.MEDIUM);
    task.setStatus(TaskStatus.WAITING);
    task.setAuthor(user);
    taskRepository.save(task);

    Comment comment = new Comment();
    comment.setTask(task);
    comment.setContent("Test comment");
    comment.setAuthor(user);
    comment.setCreatedAt(LocalDateTime.now());
    commentRepository.save(comment);

    List<Comment> comments = commentRepository.findAllByTaskId(task.getId());
    assertThat(comments).hasSize(1);
    assertThat(comments.get(0).getContent()).isEqualTo("Test comment");
  }

}

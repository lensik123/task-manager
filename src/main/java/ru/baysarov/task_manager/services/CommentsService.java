package ru.baysarov.task_manager.services;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baysarov.task_manager.exceptions.TaskNotFoundException;
import ru.baysarov.task_manager.exceptions.UserNotFoundException;
import ru.baysarov.task_manager.models.Comment;
import ru.baysarov.task_manager.models.Task;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.CommentRepository;
import ru.baysarov.task_manager.repositories.TaskRepository;
import ru.baysarov.task_manager.repositories.UserRepository;
import ru.baysarov.task_manager.util.SecurityUtils;

@Service
@Transactional
public class CommentsService {

  private final CommentRepository commentRepository;
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  @Autowired
  public CommentsService(CommentRepository commentRepository, TaskRepository taskRepository,
      UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
  }


  @Transactional
  public void addComment(int taskId, Comment comment){
    Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task with "  + taskId + " not found"));

    String userEmail = SecurityUtils.getCurrentUserEmail();
    User currentUser = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("User " + userEmail + " not found"));

    comment.setTask(task);
    comment.setAuthor(currentUser);
    comment.setCreatedAt(LocalDateTime.now());
    commentRepository.save(comment);
  }

  public List<Comment> findAllById(int taskId) {
    return commentRepository.findAllByTaskId(taskId);
  }
}

package ru.baysarov.task_manager.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.baysarov.task_manager.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

  List<Comment> findAllByTaskId(int taskId);
}

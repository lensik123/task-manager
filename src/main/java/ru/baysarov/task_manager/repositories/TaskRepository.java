package ru.baysarov.task_manager.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.baysarov.task_manager.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {


  List<Task> findAllByAssigneeId(int assigneeId);

  List<Task> findByAuthorIdOrAssigneeId(Integer authorId, Integer assigneeId, Pageable pageable);

  List<Task> findByAuthorId(Integer authorId, Pageable pageable);

  List<Task> findByAssigneeId(Integer assigneeId, Pageable pageable);

  Page<Task> findAll(Pageable pageable);

}

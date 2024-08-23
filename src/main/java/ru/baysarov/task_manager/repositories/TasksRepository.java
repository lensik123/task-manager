package ru.baysarov.task_manager.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.baysarov.task_manager.models.Task;

@Repository
public interface TasksRepository extends JpaRepository<Task, Integer> {


  List<Task> findAllByAssigneeId(int assigneeId);
}

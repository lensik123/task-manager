package ru.baysarov.task_manager.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.baysarov.task_manager.models.User;

public  interface UserRepository extends JpaRepository<User,Integer> {

  Optional<User> findByEmail(String email);
  Optional<User> findById(int id);
}

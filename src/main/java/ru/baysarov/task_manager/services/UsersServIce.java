package ru.baysarov.task_manager.services;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baysarov.task_manager.exceptions.UserNotFoundException;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.UserRepository;

@Service
@Transactional
public class UsersServIce {

  private final UserRepository userRepository;

  @Autowired
  public UsersServIce(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public Optional<User> findByEmail(String userEmail) {
    if (userRepository.findByEmail(userEmail).isPresent()) {
      return userRepository.findByEmail(userEmail);
    } else {
      throw  new UserNotFoundException("User " + userEmail + " not found");
    }
  }

}

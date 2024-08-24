package ru.baysarov.task_manager.repositories;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.enums.Role;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void whenSaveUser_thenUserIsSaved() {
    User user = User.builder()
        .firstname("John")
        .lastname("Doe")
        .email("john.doe@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();
    User savedUser = userRepository.save(user);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isGreaterThan(0);
    assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
  }

  @Test
  public void whenFindByEmail_thenReturnUser() {
    User user = User.builder()
        .firstname("Jane")
        .lastname("Doe")
        .email("jane.doe@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();
    userRepository.save(user);

    User foundUser = userRepository.findByEmail("jane.doe@example.com").orElse(null);
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getEmail()).isEqualTo("jane.doe@example.com");
  }
}

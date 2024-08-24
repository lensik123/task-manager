package ru.baysarov.task_manager.models;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void whenFindByEmail_thenReturnUser() {
    User user = User.builder()
        .firstname("John")
        .lastname("Doe")
        .email("john.doe@example.com")
        .password("Samuel1998!@")
        .role(Role.USER)
        .build();
    userRepository.save(user);

    User foundUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getEmail()).isEqualTo("john.doe@example.com");
  }
}

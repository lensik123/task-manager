package ru.baysarov.task_manager.services;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.Claims;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.baysarov.task_manager.enums.Role;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class JWTServiceTest {

  private final JWTService jwtService = new JWTService();

  @Test
  public void whenGenerateToken_thenSuccess() {
    ru.baysarov.task_manager.models.User user = ru.baysarov.task_manager.models.User.builder()
        .firstname("John")
        .lastname("Doe")
        .email("john.doe@example.com")
        .password("password")
        .role(Role.USER)
        .build();

    String token = jwtService.generateToken(user);
    assertThat(token).isNotNull();
    String username = jwtService.extractUsername(token);
    assertThat(username).isEqualTo(user.getUsername());
  }

  @Test
  public void whenIsTokenValid_thenSuccess() {
    User user = new User("john.doe@example.com", "password", new ArrayList<>());
    String token = jwtService.generateToken(user);
    boolean isValid = jwtService.isTokenValid(token, user);
    assertThat(isValid).isTrue();
  }
}

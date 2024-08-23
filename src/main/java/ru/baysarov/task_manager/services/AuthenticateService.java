package ru.baysarov.task_manager.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.baysarov.task_manager.dto.AuthenticationRequest;
import ru.baysarov.task_manager.dto.AuthenticationResponse;
import ru.baysarov.task_manager.dto.UserDTO;
import ru.baysarov.task_manager.enums.Role;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(UserDTO request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse register(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    );

    var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse
        .builder()
        .token(jwtToken)
        .build();

  }
}

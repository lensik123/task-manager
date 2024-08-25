package ru.baysarov.task_manager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.baysarov.task_manager.dto.AuthenticationRequest;
import ru.baysarov.task_manager.dto.AuthenticationResponse;
import ru.baysarov.task_manager.dto.UserDTO;
import ru.baysarov.task_manager.models.User;
import ru.baysarov.task_manager.services.AuthenticateService;
import ru.baysarov.task_manager.services.UsersServIce;
import ru.baysarov.task_manager.validators.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticateService authenticateService;
  private final UserValidator userValidator;
  private final ModelMapper mapper;
  private final UsersServIce usersServIce;

  @Operation(summary = "Register a new user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User registered successfully"),
      @ApiResponse(responseCode = "400", description = "Validation errors")
  })
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO,
      BindingResult bindingResult) {
    userValidator.validate(convertToUser(userDTO), bindingResult);
    ResponseEntity<?> errors = TasksController.getResponseEntity(bindingResult);

    if (errors != null) {
      return errors;
    }
    return ResponseEntity.ok(authenticateService.register(userDTO));
  }

  @Operation(summary = "Authenticate a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request")
  })
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    usersServIce.findByEmail(request.getEmail());
    return ResponseEntity.ok(authenticateService.register(request));
  }

  // Мапперы
  private User convertToUser(UserDTO userDTO) {
    return mapper.map(userDTO, User.class);
  }

  private UserDTO convertToUserDTO(User user) {
    return mapper.map(user, UserDTO.class);
  }
}

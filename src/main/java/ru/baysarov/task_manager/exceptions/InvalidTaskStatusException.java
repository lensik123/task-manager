package ru.baysarov.task_manager.exceptions;

public class InvalidTaskStatusException extends RuntimeException {

  public InvalidTaskStatusException(String message) {
    super(message);
  }

}

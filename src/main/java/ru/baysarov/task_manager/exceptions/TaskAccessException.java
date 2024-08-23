package ru.baysarov.task_manager.exceptions;

public class TaskAccessException extends RuntimeException {
  public TaskAccessException(String message) {
    super(message);
  }
}
package ru.baysarov.task_manager.exceptions;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(String message){
    super(message);
  }
}

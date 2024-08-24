package ru.baysarov.task_manager.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {

  @Column(nullable = false)
  @NotEmpty(message = "Comment should not be empty")
  private String content;

}

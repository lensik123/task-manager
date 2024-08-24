package ru.baysarov.task_manager.dto;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDTO {

  @Column(nullable = false)
  private String content;

}

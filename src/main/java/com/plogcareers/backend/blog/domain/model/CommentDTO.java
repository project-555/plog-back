package com.plogcareers.backend.blog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDTO {
    Long id;
    CommentUserDTO user;
    String commentContent;
    LocalDateTime createDt;
    LocalDateTime updateDt;
    List<CommentDTO> children;

}

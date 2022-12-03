package com.plogcareers.backend.blog.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.plogcareers.backend.blog.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentDTO {
    Long id;
    CommentUserDTO user;
    String commentContent;
    Boolean isSecret;
    LocalDateTime createDt;
    LocalDateTime updateDt;
    List<CommentDTO> children = new ArrayList<>();

    public Boolean isChildren(Comment comment) {
        return Objects.equals(this.id, comment.getParentCommentId());
    }
}

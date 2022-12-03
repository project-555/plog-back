package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateCommentRequest {

    Long parentCommentId;

    @NotNull
    @Length(min = 1, max = 300)
    String commentContent;

    Boolean isSecret;

    public Comment toCommentEntity(Long postingId, User user) {
        return Comment.builder()
                .commentContent(this.commentContent)
                .user(user)
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .postingId(postingId)
                .isSecret(this.isSecret)
                .parentCommentId(this.parentCommentId)
                .build();
    }
}

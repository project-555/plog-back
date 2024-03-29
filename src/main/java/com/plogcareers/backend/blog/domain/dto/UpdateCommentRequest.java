package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UpdateCommentRequest {
    @NotNull
    @Length(min = 1, max = 300)
    @ApiModelProperty(value = "수정할 댓글 내용")
    String commentContent;

    @NotNull
    @ApiModelProperty(value = "수정할 댓글 비밀 여부")
    Boolean isSecret;

    public Comment toCommentEntity(Comment comment) {
        comment.setIsSecret(this.isSecret);
        comment.setCommentContent(this.commentContent);
        comment.setUpdateDt(LocalDateTime.now());
        return comment;
    }
}

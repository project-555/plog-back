package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.ums.domain.entity.User;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "대댓글을 달 부모 댓글 ID")
    Long parentCommentID;

    @NotNull
    @Length(min = 1, max = 300)
    @ApiModelProperty(value = "댓글 내용 (길이 1~300)")
    String commentContent;

    @NotNull
    @ApiModelProperty(value = "비밀 댓글 여부")
    Boolean isSecret;

    public Comment toCommentEntity(Long postingId, User user) {
        return Comment.builder()
                .commentContent(this.commentContent)
                .user(user)
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .postingID(postingId)
                .isSecret(this.isSecret)
                .parentCommentID(this.parentCommentID)
                .build();
    }
}

package com.plogcareers.backend.blog.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.plogcareers.backend.blog.domain.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentDTO {
    @ApiModelProperty(value = "댓글 ID")
    Long id;

    @ApiModelProperty(value = "댓글 작성자")
    CommentUserDTO user;

    @ApiModelProperty(value = "댓글 내용")
    String commentContent;

    @ApiModelProperty(value = "비밀 댓글 여부")
    Boolean isSecret;

    @ApiModelProperty(value = "댓글 작성 시간")
    LocalDateTime createDt;

    @ApiModelProperty(value = "댓글 수정 시간")
    LocalDateTime updateDt;

    @Builder.Default
    @ApiModelProperty(value = "대댓글 리스트")
    List<CommentDTO> children = new ArrayList<>();

    public Boolean isChildren(Comment comment) {
        return Objects.equals(this.id, comment.getParentCommentID());
    }
}

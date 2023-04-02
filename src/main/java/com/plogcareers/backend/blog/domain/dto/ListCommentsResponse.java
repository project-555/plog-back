package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.model.CommentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
public class ListCommentsResponse {
    @ApiModelProperty(value = "댓글 리스트")
    List<CommentDTO> comments = new ArrayList<>();

    public ListCommentsResponse(List<Comment> comments, Boolean isPostingOwner, Long loginedUserID) {
        List<Comment> children = new ArrayList<>();

        for (var comment : comments) {
            // 부모 댓글일 경우 바로 리스트에 담아줌
            if (comment.getParentCommentID() == null) {
                this.comments.add(comment.toCommentDTO(isPostingOwner, loginedUserID));
                continue;
            }
            // 자식 댓글일 경우 임시 리스트에 담아둠
            children.add(comment);
        }

        // 임시 리스트에 담아둔 자식 댓글을 부모 댓글에 담아줌
        for (var commentDto : this.comments) {
            for (var comment : children) {
                if (commentDto.isChildren(comment)) {
                    commentDto.getChildren().add(comment.toCommentDTO(isPostingOwner, loginedUserID));
                }
            }
        }
    }

}

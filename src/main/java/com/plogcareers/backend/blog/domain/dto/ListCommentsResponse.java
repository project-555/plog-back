package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.model.CommentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
public class ListCommentsResponse {
    List<CommentDTO> comments = new ArrayList<>();

    public ListCommentsResponse(List<Comment> comments) {
        List<Comment> children = new ArrayList<>();
        for (var comment : comments) {
            if (comment.getParentCommentId() == null) {
                this.comments.add(comment.toCommentDTO());
                continue;
            }
            children.add(comment);
        }

        for (var commentDto : this.comments) {
            for (var comment : children) {
                if (commentDto.isChildren(comment)) {
                    commentDto.getChildren().add(comment.toCommentDTO());
                }
            }
        }
    }

}

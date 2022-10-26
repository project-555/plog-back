package com.plogcareers.backend.blog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostingCommentDTO {

    private String commentContent;
    private boolean is_secret;

}

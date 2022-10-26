package com.plogcareers.backend.blog.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostingTagDTO {
    private Long tagId;
    private String tagName;
}

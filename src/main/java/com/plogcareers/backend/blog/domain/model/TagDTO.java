package com.plogcareers.backend.blog.domain.model;

import lombok.Builder;

@Builder
public class TagDTO {
    private Long tagId;
    private String tagName;
}

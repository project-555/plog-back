package com.plogcareers.backend.blog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TagDTO {
    private Long tagId;
    private String tagName;
}

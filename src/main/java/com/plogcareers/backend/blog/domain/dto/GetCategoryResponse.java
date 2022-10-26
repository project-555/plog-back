package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryResponse {
    private Long categoryId;
    private String categoryName;
}

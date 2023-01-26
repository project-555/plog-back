package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetBlogResponse {
    private Long blogID;
    private String blogName;
    private BlogUserDTO blogUser;
    private String shortIntro;
    private String introHtml;
}

package com.plogcareers.backend.blog.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostingStarDTO {
    private Long id;
    private Long postingID;
    private PostingStarUserDTO user;
}

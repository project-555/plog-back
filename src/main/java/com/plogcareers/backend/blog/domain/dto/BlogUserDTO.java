package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlogUserDTO {
    Long userID;
    String nickname;
    String profileImageURL;
}

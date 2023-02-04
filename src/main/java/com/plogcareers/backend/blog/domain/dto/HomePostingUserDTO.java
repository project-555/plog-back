package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomePostingUserDTO {
    Long userID;
    String nickname;
}

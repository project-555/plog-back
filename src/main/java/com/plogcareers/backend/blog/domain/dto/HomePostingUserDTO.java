package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class HomePostingUserDTO {
    Long userID;
    String nickname;
}

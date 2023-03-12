package com.plogcareers.backend.blog.domain.model;
import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class HomePostingDTO {
    private Long blogID;
    private Long postingID;
    private HomePostingUserDTO homePostingUser;
    private String title;
    private String summary;
    private String thumbnailImageUrl;
    private LocalDateTime createDt;
}

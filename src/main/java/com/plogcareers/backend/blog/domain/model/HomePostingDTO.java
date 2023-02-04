package com.plogcareers.backend.blog.domain.model;
import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class HomePostingDTO {
    private Long postingID;
    private HomePostingUserDTO homePostingUser;
    private String title;
    private String summary;
    private String thumbnailImageUrl;
    private LocalDateTime createDt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomePostingDTO that)) return false;
        return Objects.equals(postingID, that.postingID) && Objects.equals(homePostingUser, that.homePostingUser) && Objects.equals(title, that.title) && Objects.equals(summary, that.summary) && Objects.equals(thumbnailImageUrl, that.thumbnailImageUrl) && Objects.equals(createDt, that.createDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postingID, homePostingUser, title, summary, thumbnailImageUrl, createDt);
    }
}

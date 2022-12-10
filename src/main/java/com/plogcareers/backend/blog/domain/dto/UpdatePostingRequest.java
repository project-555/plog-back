package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Posting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UpdatePostingRequest {

    private String title;
    private String htmlContent;
    private Category category;
    private Long stateID;
    private LocalDateTime updateDt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;

    public Posting toPostingEntity(Long id) {
        return Posting.builder()
                .id(id)
                .title(title)
                .htmlContent(htmlContent)
                .stateID(stateID)
                .updateDt(updateDt)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
    }

}

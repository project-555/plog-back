package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
public class PostingUpdateRequest {

    private String title;
    private String htmlContent;
    private Long categoryId;
    private Long stateId;
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
                .category(categoryId)
                .stateId(stateId)
                .updateDt(updateDt)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
    }

}

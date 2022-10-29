package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CreatePostingRequest {

    private Long id;
    private String title;
    private String htmlContent;
    private Long blogId;
    private Long stateId;
    private Long userId;
    private int hitCnt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;

    public Posting toEntity() {
        return Posting.builder()
                .id(id)
                .title(title)
                .htmlContent(htmlContent)
                .blogId(blogId)
                .userId(userId)
                .stateId(stateId)
                .hitCnt(hitCnt)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
    }

}
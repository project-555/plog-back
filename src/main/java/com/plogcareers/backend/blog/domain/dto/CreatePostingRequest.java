package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CreatePostingRequest {
    @NotNull
    private String title;
    private String htmlContent;
    @NotNull
    private Long blogId;
    private Long stateId;
    @NotNull
    private Long userId;
    @Max(value = 0)
    private int hitCnt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    @NotNull
    private String mdContent;
    private List<Long> tagIds;

    public Posting toEntity() {
        return Posting.builder()
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

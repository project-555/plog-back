package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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
    @NotNull
    private String htmlContent;
    @NotNull
    private Long categoryID;
    @NotNull
    private Long blogID;
    @NotNull
    @Range(min = 1, max = 3)
    private Long stateID;
    @NotNull
    private Long userID;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    @NotNull
    private String mdContent;
    private List<Long> tagIDs;

    public Posting toEntity() {
        return Posting.builder()
                .title(title)
                .htmlContent(htmlContent)
                .blogID(blogID)
                .userID(userID)
                .categoryID(categoryID)
                .stateID(stateID)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
    }

}

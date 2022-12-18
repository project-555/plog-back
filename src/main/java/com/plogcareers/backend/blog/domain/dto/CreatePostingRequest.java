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
@NoArgsConstructor
public class CreatePostingRequest {
    @NotNull
    private String title;
    @NotNull
    private String htmlContent;
    @NotNull
    private Long categoryID;
    @NotNull
    @Range(min = 1, max = 3)
    private Long stateID;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    @NotNull
    private String mdContent;
    private List<Long> tagIDs;

    public Posting toEntity(Long blogID, Long userID) {
        return Posting.builder()
                .title(this.title)
                .htmlContent(this.htmlContent)
                .blogID(blogID)
                .userID(userID)
                .categoryID(this.categoryID)
                .stateID(this.stateID)
                .isCommentAllowed(this.isCommentAllowed)
                .isStarAllowed(this.isStarAllowed)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .mdContent(this.mdContent)
                .build();
    }

}

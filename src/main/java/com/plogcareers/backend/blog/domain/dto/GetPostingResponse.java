package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GetPostingResponse {

    private Long id;
    private String title;
    private String htmlContent;
    private Long categoryID;
    private Long stateID;
    private Long hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Boolean isCommentAllowed;
    private Boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetPostingResponse that)) return false;
        return hitCnt == that.hitCnt && isCommentAllowed == that.isCommentAllowed && isStarAllowed == that.isStarAllowed && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(htmlContent, that.htmlContent) && Objects.equals(categoryID, that.categoryID) && Objects.equals(stateID, that.stateID) && Objects.equals(createDt, that.createDt) && Objects.equals(updateDt, that.updateDt) && Objects.equals(thumbnailImageUrl, that.thumbnailImageUrl) && Objects.equals(mdContent, that.mdContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, htmlContent, categoryID, stateID, hitCnt, createDt, updateDt, isCommentAllowed, isStarAllowed, thumbnailImageUrl, mdContent);
    }
}

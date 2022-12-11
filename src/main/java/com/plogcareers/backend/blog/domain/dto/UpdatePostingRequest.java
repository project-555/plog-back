package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UpdatePostingRequest {
    @NotNull
    @ApiParam(value = "포스팅 제목")
    private String title;
    @NotNull
    @ApiParam(value = "포스팅본문")
    private String htmlContent;
    @NotNull
    @ApiParam(value = "포스팅 카테고리")
    private Long categoryID;
    @NotNull
    @ApiParam(value = "포스팅 상태")
    private Long stateID;
    @NotNull
    @ApiParam(value = "댓글 허용 여부")
    Boolean isCommentAllowed;
    @NotNull
    @ApiParam(value = "좋아요 허용 여부")
    Boolean isStarAllowed;
    @ApiParam(value = "포스팅 썸네일")
    private String thumbnailImageUrl;
    private String mdContent;

    public Posting toPostingEntity(Posting posting) {
        posting.setTitle(this.title);
        posting.setHtmlContent(this.htmlContent);
        posting.setMdContent(this.mdContent);
        posting.setCategoryID(this.categoryID);
        posting.setStateID(this.stateID);
        posting.setThumbnailImageUrl(this.thumbnailImageUrl);
        posting.setIsStarAllowed(this.isStarAllowed);
        posting.setIsCommentAllowed(this.isCommentAllowed);
        posting.setUpdateDt(LocalDateTime.now());
        return posting;
    }

}

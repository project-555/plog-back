package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "포스팅 ID")
    private Long id;

    @ApiModelProperty(value = "포스팅 제목")
    private String title;

    @ApiModelProperty(value = "포스팅 내용 HTML")
    private String htmlContent;

    @ApiModelProperty(value = "포스팅 내용 Markdown")
    private String mdContent;

    @ApiModelProperty(value = "포스팅 카테고리 ID")
    private Long categoryID;

    @ApiModelProperty(value = "포스팅 상태 ID")
    private Long stateID;

    @ApiModelProperty(value = "포스팅 조회수")
    private Long hitCnt;

    @ApiModelProperty(value = "포스팅 생성일")
    private LocalDateTime createDt;

    @ApiModelProperty(value = "포스팅 수정일")
    private LocalDateTime updateDt;

    @ApiModelProperty(value = "포스팅 댓글 허용 여부")
    private Boolean isCommentAllowed;

    @ApiModelProperty(value = "포스팅 좋아요 허용 여부")
    private Boolean isStarAllowed;

    @ApiModelProperty(value = "포스팅 썸네일 이미지 URL")
    private String thumbnailImageUrl;

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

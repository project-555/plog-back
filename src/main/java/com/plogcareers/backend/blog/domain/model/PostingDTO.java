package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostingDTO {
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

    @ApiModelProperty(value = "포스팅 카테고리 이름")
    private Long stateID;

    @ApiModelProperty(value = "포스팅 조회 수")
    private Long hitCnt;

    @ApiModelProperty(value = "포스팅 좋아요 수")
    private Long starCnt;

    @ApiModelProperty(value = "포스팅 생성 일시")
    private LocalDateTime createDt;

    @ApiModelProperty(value = "포스팅 수정 일시")
    private LocalDateTime updateDt;

    @ApiModelProperty(value = "포스팅의 덧글 허용 여부")
    private Boolean isCommentAllowed;

    @ApiModelProperty(value = "포스팅의 좋아요 허용 여부")
    private Boolean isStarAllowed;

    @ApiModelProperty(value = "포스팅의 썸네일 이미지 URL")
    private String thumbnailImageURL;
    
}

package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "제목", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(value = "HTML 형식의 내용", required = true)
    private String htmlContent;

    @NotNull
    @ApiModelProperty(value = "카테고리 ID", required = true)
    private Long categoryID;

    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "상태 ID (1: 임시저장, 2: 공개, 3: 비공개)", required = true)
    private Long stateID;

    @NotNull
    @ApiModelProperty(value = "댓글 허용 여부", required = true)
    private Boolean isCommentAllowed;

    @NotNull
    @ApiModelProperty(value = "좋아요 허용 여부", required = true)
    private Boolean isStarAllowed;

    @ApiModelProperty(value = "썸네일 이미지 URL")
    private String thumbnailImageUrl;

    @NotNull
    @ApiModelProperty(value = "Markdown 형식의 내용", required = true)
    private String mdContent;

    @ApiModelProperty(value = "포스팅에 달 태그 ID 리스트")
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

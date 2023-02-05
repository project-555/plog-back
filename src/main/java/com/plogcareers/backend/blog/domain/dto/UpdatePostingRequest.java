package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UpdatePostingRequest {
    @NotNull
    @ApiModelProperty(value = "수정할 댓글 허용 여부")
    Boolean isCommentAllowed;

    @NotNull
    @ApiModelProperty(value = "수정할 좋아요 허용 여부")
    Boolean isStarAllowed;

    @NotNull
    @Length(min = 1, max = 200)
    @ApiModelProperty(value = "수정할 포스팅 제목")
    private String title;

    @NotNull
    @ApiModelProperty(value = "수정할 포스팅 본문 HTML")
    @Length(min = 1, max = 10000)
    private String htmlContent;

    @NotNull
    @ApiModelProperty(value = "수정할 포스팅 본문 Markdown")
    private String mdContent;

    @NotNull
    @ApiModelProperty(value = "수정할 포스팅 카테고리")
    private Long categoryID;

    @NotNull
    @ApiModelProperty(value = "수정할 포스팅 상태")
    @Range(min = 1, max = 2)
    private Long stateID;

    @ApiModelProperty(value = "수정할 포스팅 썸네일")
    private String thumbnailImageUrl;

    @ApiModelProperty(value = "수정할 포스팅 태그 ID 리스트")
    private List<Long> tagIDs;

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

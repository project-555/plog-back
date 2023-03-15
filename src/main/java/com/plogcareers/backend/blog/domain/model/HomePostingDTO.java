package com.plogcareers.backend.blog.domain.model;

import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class HomePostingDTO {
    @ApiModelProperty(value = "블로그 ID", required = true, example = "1")
    private Long blogID;

    @ApiModelProperty(value = "포스팅 ID", required = true, example = "1")
    private Long postingID;

    @ApiModelProperty(value = "포스팅 작성자", required = true)
    private HomePostingUserDTO homePostingUser;

    @ApiModelProperty(value = "포스팅 제목", required = true, example = "포스팅 제목")
    private String title;

    @ApiModelProperty(value = "포스팅 요약", required = true, example = "포스팅 요약")
    private String summary;

    @ApiModelProperty(value = "포스팅 썸네일 이미지 URL", example = "https://plogcareers.com/thumbnail.png")
    private String thumbnailImageURL;

    @ApiModelProperty(value = "포스팅 좋아요 수", required = true, example = "1")
    private Long starCnt;

    @ApiModelProperty(value = "포스팅 조회 수", required = true, example = "1")
    private Long hitCnt;

    @ApiModelProperty(value = "포스팅 작성일", required = true, example = "2019-01-01 00:00:00")
    private LocalDateTime createDt;
}

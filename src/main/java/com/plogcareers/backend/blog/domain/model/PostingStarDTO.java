package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class PostingStarDTO {
    @ApiModelProperty(value = "포스팅 좋아요 ID")
    private Long id;

    @ApiModelProperty(value = "좋아요를 남긴 포스팅 ID")
    private Long postingID;

    @ApiModelProperty(value = "좋아요를 남긴 유저 ID")
    private PostingStarUserDTO user;
}

package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostingStarUserDTO {
    @ApiModelProperty(value = "포스팅에 좋아요를 남긴 유저 ID")
    Long id;
    @ApiModelProperty(value = "포스팅에 좋아요를 남긴 유저 닉네임")
    String nickname;
}

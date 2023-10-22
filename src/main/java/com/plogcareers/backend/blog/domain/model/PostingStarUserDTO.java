package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class PostingStarUserDTO {
    @ApiModelProperty(value = "포스팅에 좋아요를 남긴 유저 ID")
    Long id;

    @ApiModelProperty(value = "포스팅에 좋아요를 남긴 유저 닉네임")
    String nickname;

    @ApiModelProperty(value = "포스팅에 좋아요를 남긴 유저 프로필 이미지 URL")
    String profileImageURL;
}

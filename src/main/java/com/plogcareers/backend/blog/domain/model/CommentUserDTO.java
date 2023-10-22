package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class CommentUserDTO {
    @ApiModelProperty(value = "덧글을 남긴 유저 아이디")
    Long userID;
    @ApiModelProperty(value = "덧글을 남긴 유저 닉네임")
    String nickname;
    @ApiModelProperty(value = "덧글을 남긴 유저 프로필 이미지 URL")
    String profileImageURL;
}

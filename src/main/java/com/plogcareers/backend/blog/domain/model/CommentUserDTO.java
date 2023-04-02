package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentUserDTO {
    @ApiModelProperty(value = "덧글을 남긴 유저 아이디")
    Long userID;
    @ApiModelProperty(value = "덧글을 남긴 유저 닉네임")
    String nickname;
}

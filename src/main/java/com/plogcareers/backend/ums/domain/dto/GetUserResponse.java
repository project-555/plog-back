package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetUserResponse {
    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "프로필 이미지 URL")
    private String profileImageURL;

    @ApiModelProperty(value = "블로그 제목")
    private String blogName;

    @ApiModelProperty(value = "블로그 소개글")
    private String shortIntro;

    @ApiModelProperty(value = "블로그 소개 HTML")
    private String introHTML;
}

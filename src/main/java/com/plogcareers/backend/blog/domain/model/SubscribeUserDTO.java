package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class SubscribeUserDTO {
    @ApiModelProperty(value = "구독한 블로그 유저 ID")
    public Long blogUserID;

    @ApiModelProperty(value = "구독한 블로그 유저 닉네임")
    public String nickname;

    @ApiModelProperty(value = "구독한 블로그 유저 프로필 이미지 url")
    public String profileImageURL;
}

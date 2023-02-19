package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubscribeDTO {
    @ApiModelProperty(value = "구독 정보 ID")
    public Long id;

    @ApiModelProperty(value = "구독한 블로그 ID")
    public Long blog_id;

    @ApiModelProperty(value = "구독한 블로그 유저 ID")
    public Long blog_user_id;

    @ApiModelProperty(value = "구독한 블로그 이름")
    public String blog_name;

    @ApiModelProperty(value = "구독한 블로그 유저 닉네임")
    public String nickname;

    @ApiModelProperty(value = "구독한 블로그 유저 프로필 이미지 url")
    public String profile_image_url;
}

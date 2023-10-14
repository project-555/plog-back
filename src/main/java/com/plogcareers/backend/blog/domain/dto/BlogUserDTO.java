package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class BlogUserDTO {
    @ApiModelProperty(value = "블로그 주인 유저 ID")
    Long userID;
    @ApiModelProperty(value = "블로그 주인 닉네임")
    String nickname;
    @ApiModelProperty(value = "블로그 주인 프로필 이미지 URL")
    String profileImageURL;
}

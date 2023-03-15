package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class HomePostingUserDTO {
    @ApiModelProperty(value = "유저 ID", required = true, example = "1")
    Long userID;
    @ApiModelProperty(value = "유저 닉네임", required = true, example = "plog")
    String nickname;
    @ApiModelProperty(value = "유저 프로필 이미지 URL", example = "https://plogcareers.com/profile.png")
    String profileImageURL;
}

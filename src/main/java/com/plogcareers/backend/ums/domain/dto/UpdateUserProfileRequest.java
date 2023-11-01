package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.ums.domain.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UpdateUserProfileRequest {
    @NotNull
    @ApiModelProperty(value = "수정을 요청하는 사용자 ID")
    private Long userID;

    @NotEmpty
    @ApiModelProperty(value = "수정할 닉네임")
    private String nickname;

    @ApiModelProperty(value = "수정할 프로필 사진")
    private String profileImageURL;

    public User toUserEntity(User user) {
        user.setNickname(this.nickname);
        user.setProfileImageURL(this.profileImageURL);
        return user;
    }
}

package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.ums.domain.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateUserPasswordRequest {
    @ApiModelProperty(value = "비밀번호 확인이 필요한 사용자 ID")
    private Long userID;

    @ApiModelProperty(value = "사용자가 비밀번호 확인을 위해 입력한 비밀번호")
    private String password;

    @ApiModelProperty(value = "사용자가 변경하려는 새 비밀번호")
    private String newPassword;

    public User toUserEntity(User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(this.newPassword));
        return user;
    }
}

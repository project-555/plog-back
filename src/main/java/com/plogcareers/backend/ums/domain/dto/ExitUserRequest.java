package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExitUserRequest {
    @ApiModelProperty(value = "회원탈퇴를 하려는 사용자 ID")
    private Long userID;
}

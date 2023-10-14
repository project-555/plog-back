package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckBlogNameExistRequest {
    @ApiModelProperty(value = "중복 확인하고자 하는 블로그 명 (영소문자, 숫자, 특수문자(\"-\"\"_\")만 가능)", required = true, example = "plogcareers")
    @Pattern(regexp = "^[a-z0-9_-]{4,20}$", message = "블로그 이름은 영문 소문자와 숫자, 특수문자(\"_\"\"-\")로만 구성되어야 합니다. (4~20자)")
    String blogName;
}

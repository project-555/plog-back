package com.plogcareers.backend.ums.domain.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckBlogNameExistRequest {
    @ApiParam(value = "중복 확인하고자 하는 블로그 명 (영소문자, 숫자만 가능)", required = true, example = "plogcareers")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "블로그 이름은 영문 소문자와 숫자로만 구성되어야 합니다. (4~20자)")
    String blogName;
}

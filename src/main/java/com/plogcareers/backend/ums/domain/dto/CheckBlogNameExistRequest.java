package com.plogcareers.backend.ums.domain.dto;

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
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "블로그 이름은 영문 소문자와 숫자로만 구성되어야 합니다. (4~20자)")
    String blogName;
}

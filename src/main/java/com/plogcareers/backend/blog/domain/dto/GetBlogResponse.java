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
public class GetBlogResponse {
    @ApiModelProperty(value = "블로그 ID")
    private Long blogID;
    @ApiModelProperty(value = "블로그 이름")
    private String blogName;
    @ApiModelProperty(value = "블로그 주인")
    private BlogUserDTO blogUser;
    @ApiModelProperty(value = "블로그 짧은 소개글")
    private String shortIntro;
    @ApiModelProperty(value = "블로그 소개 HTML")
    private String introHTML;
}

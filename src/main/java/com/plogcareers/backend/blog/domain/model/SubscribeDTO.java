package com.plogcareers.backend.blog.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class SubscribeDTO {
    @ApiModelProperty(value = "구독 정보 ID")
    public Long id;

    @ApiModelProperty(value = "구독한 블로그 ID")
    public Long blogId;

    @ApiModelProperty(value = "구독한 블로그 이름")
    public String blogName;

    @ApiModelProperty(value = "구독한 블로그 유저")
    public SubscribeUserDTO user;

}

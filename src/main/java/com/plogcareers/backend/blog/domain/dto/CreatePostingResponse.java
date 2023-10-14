package com.plogcareers.backend.blog.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreatePostingResponse {
    @ApiModelProperty(value = "생성된 포스팅 ID", example = "1")
    private Long postingID;
}

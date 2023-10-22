package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ListPostingTagResponse {
    @ApiModelProperty(value = "포스팅 태그 리스트")
    List<PostingTagDTO> postingTags;
}

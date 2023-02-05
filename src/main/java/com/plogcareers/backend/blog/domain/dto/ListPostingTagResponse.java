package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListPostingTagResponse {
    @ApiModelProperty(value = "포스팅 태그 리스트")
    List<PostingTagDTO> postingTags;
}

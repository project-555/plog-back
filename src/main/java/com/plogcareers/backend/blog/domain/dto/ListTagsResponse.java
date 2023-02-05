package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.TagDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListTagsResponse {
    @ApiModelProperty(value = "태그 리스트")
    List<TagDTO> tags;
}

package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.TagDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ListTagsResponse {
    @ApiModelProperty(value = "태그 리스트")
    List<TagDTO> tags;
}

package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ListPostingsResponse {
    @ApiModelProperty(value = "포스팅 리스트")
    List<PostingDTO> postings;
}

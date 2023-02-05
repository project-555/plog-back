package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingStarDTO;
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
public class ListPostingStarsResponse {

    @ApiModelProperty(value = "포스팅 좋아요 리스트")
    List<PostingStarDTO> postingStars;
}

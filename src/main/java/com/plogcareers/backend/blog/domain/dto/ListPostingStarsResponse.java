package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingStarDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ListPostingStarsResponse {

    @ApiModelProperty(value = "포스팅 좋아요 리스트")
    List<PostingStarDTO> postingStars;
}

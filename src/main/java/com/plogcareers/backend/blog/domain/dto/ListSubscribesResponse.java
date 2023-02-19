package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.SubscribeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ListSubscribesResponse {
    @ApiModelProperty(value = "구독 리스트")
    List<SubscribeDTO> subscribes;
}

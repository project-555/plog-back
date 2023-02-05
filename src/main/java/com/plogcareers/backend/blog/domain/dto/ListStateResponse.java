package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.StateDTO;
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
public class ListStateResponse {
    @ApiModelProperty(value = "포스팅에 적용 가능한 상태 리스트")
    List<StateDTO> states;
}

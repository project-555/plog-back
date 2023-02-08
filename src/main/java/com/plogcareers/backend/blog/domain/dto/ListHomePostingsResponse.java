package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.HomePostingDTO;
import lombok.*;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ListHomePostingsResponse {
    List<HomePostingDTO> homePostings;
}

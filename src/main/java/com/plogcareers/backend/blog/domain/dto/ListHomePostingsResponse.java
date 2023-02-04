package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.HomePostingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListHomePostingsResponse {
    List<HomePostingDTO> homePostings;

    @Override
    public boolean equals(Object obj) {
        return this.homePostings.equals(obj);
    }
}

package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ListPostingsResponse {
    List<PostingDTO> postings;
}

package com.plogcareers.backend.blog.domain.dto;

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

package com.plogcareers.backend.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListPostingsRequest {
    String search;
    List<Long> tagIDs;
    Long categoryID;
}

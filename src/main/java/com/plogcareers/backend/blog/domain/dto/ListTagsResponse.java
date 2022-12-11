package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListTagsResponse {
    List<TagDTO> tags;
}

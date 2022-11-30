package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListPostingTagResponse {
    List<PostingTagDTO> postingTags;
}

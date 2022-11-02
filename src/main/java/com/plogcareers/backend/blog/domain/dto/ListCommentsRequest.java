package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.common.domain.dto.OPagingRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ListCommentsRequest extends OPagingRequest {
    private Long postingId;
}

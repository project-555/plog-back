package com.plogcareers.backend.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
abstract class CursorPagingRequest {
    public Long lastCursorID = 99999999999999999L;
    public Long pageSize = 50L;
}

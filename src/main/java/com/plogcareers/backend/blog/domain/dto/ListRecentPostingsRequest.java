package com.plogcareers.backend.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListRecentPostingsRequest {
    Long lastPostingID;
    Integer pageSize;
}

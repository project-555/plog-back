package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CountPostingsResponse {
    private Long count;
}

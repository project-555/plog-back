package com.plogcareers.backend.blog.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StateDTO {
    public Long id;
    public String stateName;
}

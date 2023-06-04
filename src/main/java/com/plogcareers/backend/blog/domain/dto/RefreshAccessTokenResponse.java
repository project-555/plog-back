package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.ums.domain.dto.Token;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RefreshAccessTokenResponse {
    public Token token;
}

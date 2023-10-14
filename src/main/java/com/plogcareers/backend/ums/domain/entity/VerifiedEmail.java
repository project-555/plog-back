package com.plogcareers.backend.ums.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RedisHash("verifiedEmail")
public class VerifiedEmail {
    @Id
    String email;
    String verifyToken;
    @TimeToLive
    Long expiredTime;
}

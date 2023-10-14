package com.plogcareers.backend.ums.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@RedisHash("emailVerifyCode")
public class EmailVerifyCode {
    @Id
    private String email;

    private String verifyCode;

    @TimeToLive
    private Long expiredTime;
}

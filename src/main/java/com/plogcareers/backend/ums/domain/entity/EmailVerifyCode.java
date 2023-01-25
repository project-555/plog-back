package com.plogcareers.backend.ums.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@Getter
@Setter
@Builder
@RedisHash("emailVerifyCode")
public class EmailVerifyCode {
    @Id
    private String email;

    private String verifyCode;

    @TimeToLive
    private Long expiredTime;
}

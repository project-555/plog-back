package com.plogcareers.backend.ums.repository.redis;

import com.plogcareers.backend.ums.domain.entity.EmailVerifyCode;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public interface EmailVerifyCodeRepository extends KeyValueRepository<EmailVerifyCode, String> {
}

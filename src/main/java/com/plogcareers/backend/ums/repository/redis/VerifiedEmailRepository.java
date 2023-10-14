package com.plogcareers.backend.ums.repository.redis;

import com.plogcareers.backend.ums.domain.entity.VerifiedEmail;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public interface VerifiedEmailRepository extends KeyValueRepository<VerifiedEmail, String> {
}

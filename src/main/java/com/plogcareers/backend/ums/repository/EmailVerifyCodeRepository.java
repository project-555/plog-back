package com.plogcareers.backend.ums.repository;

import com.plogcareers.backend.ums.domain.entity.EmailVerifyCode;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface EmailVerifyCodeRepository extends KeyValueRepository<EmailVerifyCode, String> {

}

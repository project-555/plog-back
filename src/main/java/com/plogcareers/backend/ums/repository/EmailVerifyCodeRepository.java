package com.plogcareers.backend.ums.repository;

import com.plogcareers.backend.ums.domain.entity.EmailVerifyCode;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerifyCodeRepository extends CrudRepository<EmailVerifyCode, String> {

}

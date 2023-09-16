package com.plogcareers.backend.ums.repository;

import com.plogcareers.backend.ums.domain.entity.VerifiedEmail;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface VerifiedEmailRepository extends KeyValueRepository<VerifiedEmail, String> {
}

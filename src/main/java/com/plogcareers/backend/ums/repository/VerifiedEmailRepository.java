package com.plogcareers.backend.ums.repository;

import com.plogcareers.backend.ums.domain.entity.VerifiedEmail;
import org.springframework.data.repository.CrudRepository;

public interface VerifiedEmailRepository extends CrudRepository<VerifiedEmail, String> {
}

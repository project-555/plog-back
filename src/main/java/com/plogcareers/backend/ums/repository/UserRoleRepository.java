package com.plogcareers.backend.ums.repository;

import com.plogcareers.backend.ums.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

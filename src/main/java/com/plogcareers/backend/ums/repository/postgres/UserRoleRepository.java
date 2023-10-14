package com.plogcareers.backend.ums.repository.postgres;

import com.plogcareers.backend.ums.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

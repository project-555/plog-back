package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
}

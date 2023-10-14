package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
}

package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

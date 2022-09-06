package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}

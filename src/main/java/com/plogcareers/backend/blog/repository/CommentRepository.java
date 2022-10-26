package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostingIdAndParentIsNullOrderByCreateDtDesc(Long postingId);
}

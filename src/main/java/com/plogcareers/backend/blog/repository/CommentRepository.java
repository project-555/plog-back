package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostingIdAndParentIsNullOrderByCreateDtDesc(Long postingId, Pageable pageable);
}

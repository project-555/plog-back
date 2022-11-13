package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.ums.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostingIdAndParentIsNullAndUserAndIsSecretOrIsSecretOrderByUpdateDtDesc(Long postingId, User user, boolean secret, boolean secret2, Pageable pageable);

    Page<Comment> findByPostingIdAndParentIsNullOrderByUpdateDtDesc(Long postingId, Pageable pageable);
}

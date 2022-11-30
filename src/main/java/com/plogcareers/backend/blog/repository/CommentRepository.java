package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.ums.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    List<Comment> findByUserAndGuest(Long postingId, User user);

    List<Comment> findByBlogOwner(Long postingId);
}

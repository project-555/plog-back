package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {

    Boolean existsByUserIdAndBlogId(Long userId, Long blogId);
}
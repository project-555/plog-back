package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {

    Optional<Subscribe> findFirstByUserIdAndBlogId(Long userId, Long blogId);
}

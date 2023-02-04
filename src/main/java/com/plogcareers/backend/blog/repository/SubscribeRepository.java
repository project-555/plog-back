package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {

    Boolean existsByUserIdAndBlogId(Long userId, Long blogId);
    List<Subscribe> findByUserId(Long userId);

}
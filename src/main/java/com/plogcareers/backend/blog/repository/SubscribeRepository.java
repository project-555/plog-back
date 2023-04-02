package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    Boolean existsByUserIDAndBlogId(Long userID, Long blogId);

    List<Subscribe> findByUserID(Long userID);

}

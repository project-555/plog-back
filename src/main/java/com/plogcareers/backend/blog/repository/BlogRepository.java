package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.ums.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Boolean existsByBlogName(String blogName);

    List<Blog> findByUser(User user);
}

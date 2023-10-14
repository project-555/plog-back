package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.ums.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Boolean existsByBlogName(String blogName);

    List<Blog> findByUser(User user);
}

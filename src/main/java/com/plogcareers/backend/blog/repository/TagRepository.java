package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByBlogId(Long blogId);
}

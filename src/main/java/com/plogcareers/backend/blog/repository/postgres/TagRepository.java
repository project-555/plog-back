package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByBlogID(Long blogID);

    Boolean existsByBlogIDAndTagName(Long blogID, String tagName);

    List<Tag> findByIdIn(List<Long> id);
}

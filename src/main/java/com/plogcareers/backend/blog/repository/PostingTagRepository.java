package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {
    List<PostingTag> findByPostingId(Long postingID);

    List<PostingTag> findByTag_IdIn(List<Long> tagIDs);

    void deleteAllByPosting(Posting posting);
}

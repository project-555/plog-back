package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.PostingStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingStarRepository extends JpaRepository<PostingStar, Long> {

    List<PostingStar> findByPostingID(Long postingID);

    Boolean existsByPostingIDAndUserId(Long postingID, Long userID);

    Optional<PostingStar> findFirstByPostingIDAndUserId(Long postingID, Long userID);
}

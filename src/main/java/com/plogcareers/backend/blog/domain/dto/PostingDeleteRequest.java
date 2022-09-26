package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingDelete;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PostingDeleteRequest {

    private Long id;

    public PostingDelete toEntity() {

        return PostingDelete.builder()
                .id(id)
                .build();
    }

    @Builder
    public PostingDeleteRequest(Long id) {
        this.id = id;
    }
}

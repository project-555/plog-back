package com.plogcareers.backend.blog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostingDTO {
    private Long id;
    private String title;
    private String htmlContent;
    private String mdContent;
    private Long categoryID;
    private Long stateID;
    private Long hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Boolean isCommentAllowed;
    private Boolean isStarAllowed;
    private String thumbnailImageUrl;
    private Long postingStarCount;
}

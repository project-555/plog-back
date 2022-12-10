package com.plogcareers.backend.blog.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GetPostingResponse {

    private Long id;
    private String title;
    private String htmlContent;
    private Long categoryID;
    private Long stateID;
    private int hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;


}

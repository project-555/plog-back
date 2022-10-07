package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PostingDetailResponse {

    private Long id;
    private String title;
    private String htmlContent;
    private String userNickname;
    private PostingDetailCategoryDto category;
    private Long stateId;
    private int hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;


}

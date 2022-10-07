package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.blog.domain.entity.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PostingDetailRequest {

    private Long id;
    private String title;
    private String htmlContent;
    private Long userId;
    private Long categoryId;
    private Long blogId;
    private Long stateId;
    private int hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;

    public Posting toEntity(User user, Category category) {

        return Posting.builder()
                .id(id)
                .title(title)
                .htmlContent(htmlContent)
                .user(user)
                .category(category)
                .blogId(blogId)
                .stateId(stateId)
                .hitCnt(hitCnt)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
    }

}

package com.plogcareers.backend.post.dto;

import com.plogcareers.backend.post.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String htmlContent;
    private int userId;
    private int categoryId;
    private int blogId;
    private int stateId;
    private int hitCnt;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private boolean isCommentAllowed;
    private boolean isStarAllowed;
    private String thumbnailImageUrl;
    private String mdContent;

    public Post toEntity() {
        Post post = Post.builder()
                .id(id)
                .title(title)
                .htmlContent(htmlContent)
                .userId(userId)
                .categoryId(categoryId)
                .blogId(blogId)
                .stateId(stateId)
                .hitCnt(hitCnt)
                .createDt(createDt)
                .updateDt(updateDt)
                .isCommentAllowed(isCommentAllowed)
                .isStarAllowed(isStarAllowed)
                .thumbnailImageUrl(thumbnailImageUrl)
                .mdContent(mdContent)
                .build();
        return post;
    }

    @Builder
    public PostDto(Long id, String title, String htmlContent, int userId, int categoryId, int blogId, int stateId, int hitCnt, LocalDateTime createDt, LocalDateTime updateDt, boolean isCommentAllowed, boolean isStarAllowed, String thumbnailImageUrl, String mdContent) {
        this.id = id;
        this.title = title;
        this.htmlContent = htmlContent;
        this.userId = userId;
        this.categoryId = categoryId;
        this.blogId = blogId;
        this.stateId = stateId;
        this.hitCnt = hitCnt;
        this.createDt = createDt;
        this.updateDt = updateDt;
        this.isCommentAllowed = isCommentAllowed;
        this.isStarAllowed = isStarAllowed;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.mdContent = mdContent;
    }
}
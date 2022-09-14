package com.plogcareers.backend.blog.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "posting", schema = "plog_blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "html_content")
    private String htmlContent;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Column(name = "blog_id", nullable = false)
    private int blogId;

    @Column(name = "state_id")
    private int stateId;

    @Column(name = "hit_cnt", columnDefinition = "0")
    private int hitCnt;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @Column(name = "is_comment_allowed", columnDefinition = "true")
    private boolean isCommentAllowed;

    @Column(name = "is_star_allowed", columnDefinition = "true")
    private boolean isStarAllowed;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "md_content", nullable = false)
    private String mdContent;


    @Builder
    public Posting(Long id, String title, String htmlContent, int userId, int categoryId, int blogId, int stateId, int hitCnt, LocalDateTime createDt, LocalDateTime updateDt, boolean isCommentAllowed, boolean isStarAllowed, String thumbnailImageUrl, String mdContent) {
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

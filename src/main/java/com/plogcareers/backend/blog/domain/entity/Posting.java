package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "posting", schema = "plog_blog")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "html_content")
    private String htmlContent;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "blog_id", nullable = false)
    private Long blogId;

    @Column(name = "state_id")
    private Long stateId;

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

    public GetPostingResponse toPostingDetailResponse() {
        return GetPostingResponse.builder()
                .id(this.id)
                .htmlContent(this.htmlContent)
                .mdContent(this.mdContent)
                .isStarAllowed(this.isStarAllowed)
                .isCommentAllowed(this.isCommentAllowed)
                .updateDt(this.updateDt)
                .createDt(this.createDt)
                .build();
    }
}

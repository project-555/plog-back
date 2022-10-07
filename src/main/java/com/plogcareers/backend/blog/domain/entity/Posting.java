package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.PostingDetailResponse;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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

    public PostingDetailResponse toPostingDetailResponse() {
        return PostingDetailResponse.builder()
                .id(this.id)
                .htmlContent(this.htmlContent)
                .mdContent(this.mdContent)
                .category(this.category.toPostingDetailCategoryDto())
                .isStarAllowed(this.isStarAllowed)
                .isCommentAllowed(this.isCommentAllowed)
                .userNickname(this.user.getNickname())
                .updateDt(this.updateDt)
                .createDt(this.createDt)
                .build();
    }
}

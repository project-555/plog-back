package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.PostingDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "v_posting", schema = "plog_blog")
@NoArgsConstructor
@AllArgsConstructor
public class VPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "html_content")
    private String htmlContent;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "category_id")
    private Long categoryID;

    @Column(name = "blog_id", nullable = false)
    private Long blogID;

    @Column(name = "state_id")
    private Long stateID;

    @Column(name = "hit_cnt", columnDefinition = "0")
    private Long hitCnt;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @Column(name = "is_comment_allowed", columnDefinition = "true")
    private Boolean isCommentAllowed;

    @Column(name = "is_star_allowed", columnDefinition = "true")
    private Boolean isStarAllowed;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "md_content", nullable = false)
    private String mdContent;

    @Column(name = "posting_star_count")
    private Long postingStarCount;

    public PostingDTO toPostingDTO() {
        return PostingDTO.builder()
                .id(this.id)
                .title(this.title)
                .mdContent(this.mdContent)
                .htmlContent(this.htmlContent)
                .stateID(this.stateID)
                .categoryID(this.categoryID)
                .isStarAllowed(this.isStarAllowed)
                .isCommentAllowed(this.isCommentAllowed)
                .hitCnt(this.hitCnt)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .createDt(this.createDt)
                .updateDt(this.updateDt)
                .postingStarCount(this.postingStarCount)
                .build();
    }
}

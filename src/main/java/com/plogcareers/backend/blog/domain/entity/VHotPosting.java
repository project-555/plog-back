package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;
import com.plogcareers.backend.blog.domain.model.HomePostingDTO;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "v_hot_posting", schema = "plog_blog")
@NoArgsConstructor
@AllArgsConstructor
public class VHotPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "html_content")
    private String htmlContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "star_row_num", nullable = false)
    private String starRowNum;


    public HomePostingDTO toHomePostingDTO() {
        return HomePostingDTO.builder()
                .postingID(this.id)
                .homePostingUser(this.user.toHomePostingUserDTO())
                .title(this.title)
                .summary(this.summary)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .createDt(this.createDt)
                .build();
    }
}

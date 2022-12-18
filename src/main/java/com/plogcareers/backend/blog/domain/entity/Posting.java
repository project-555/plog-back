package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public GetPostingResponse toGetPostingResponse() {
        return GetPostingResponse.builder()
                .id(this.id)
                .title(this.title)
                .htmlContent(this.htmlContent)
                .stateID(this.stateID)
                .updateDt(this.updateDt)
                .isStarAllowed(this.isStarAllowed)
                .isCommentAllowed(this.isCommentAllowed)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .mdContent(this.mdContent)
                .build();
    }


    public Boolean hasComment(Comment comment) {
        return comment.getPostingID().equals(this.id);
    }

    public Boolean isOwner(Long loginedUserID) {
        return this.userID.equals(loginedUserID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posting posting)) return false;
        return Objects.equals(id, posting.id) &&
                Objects.equals(title, posting.title) &&
                Objects.equals(htmlContent, posting.htmlContent) &&
                Objects.equals(userID, posting.userID) &&
                Objects.equals(categoryID, posting.categoryID) &&
                Objects.equals(blogID, posting.blogID) &&
                Objects.equals(stateID, posting.stateID) &&
                Objects.equals(hitCnt, posting.hitCnt) &&
                Objects.equals(createDt, posting.createDt) &&
                Objects.equals(updateDt, posting.updateDt) &&
                Objects.equals(isCommentAllowed, posting.isCommentAllowed) &&
                Objects.equals(isStarAllowed, posting.isStarAllowed) &&
                Objects.equals(thumbnailImageUrl, posting.thumbnailImageUrl) &&
                Objects.equals(mdContent, posting.mdContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, htmlContent, userID, categoryID, blogID, stateID, hitCnt, createDt, updateDt, isCommentAllowed, isStarAllowed, thumbnailImageUrl, mdContent);
    }
}

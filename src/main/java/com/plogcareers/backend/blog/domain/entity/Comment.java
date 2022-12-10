package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.CommentDTO;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "comment", schema = "plog_blog")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Builder
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "posting_id")
    private Long postingID;

    @Column(name = "parent_comment_id")
    private Long parentCommentID;

    @Column(name = "depth")
    private Long depth;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "is_secret", nullable = false)
    private Boolean isSecret;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    public CommentDTO toCommentDTO(Boolean isPostingOwner, Long loginedUserId) {
        if (this.isSecret && !isPostingOwner && !this.isOwner(loginedUserId)) {
            this.setCommentContent("작성자가 비공개로 표시한 덧글입니다.");
            this.user.setNickname(this.user.getNickname().charAt(0) + "****");
        }
        return CommentDTO.builder()
                .id(this.id)
                .commentContent(this.commentContent)
                .updateDt(this.updateDt)
                .user(this.user.toCommentUserDTO())
                .isSecret(this.isSecret)
                .createDt(this.createDt)
                .children(new ArrayList<>())
                .build();
    }

    public Boolean isOwner(Long userID) {
        return this.user.getId().equals(userID);
    }
}

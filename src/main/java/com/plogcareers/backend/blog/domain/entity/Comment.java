package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.CommentDTO;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "comment", schema = "plog_blog")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "posting_id")
    private Long postingId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "depth")
    private Long depth;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommentDTO toCommentDTO() {
        return CommentDTO.builder()
                .id(this.id)
                .commentContent(this.commentContent)
                .updateDt(this.updateDt)
                .user(this.user.toCommentUserDTO())
                .createDt(this.createDt)
                .children(new ArrayList<>())
                .build();
    }
}

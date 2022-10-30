package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.CommentDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment", schema = "plog_blog")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "posting_id")
    private Long postingId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Comment> children = new ArrayList<>();

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

    public CommentDTO toCommentDTO() {
        return CommentDTO.builder()
                .commentContent(this.commentContent)
                .updateDt(this.updateDt)
                .createDt(this.createDt)
                .children(children.stream().map(Comment::toCommentDTO).toList())
                .build();
    }
}

package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.ums.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog", schema = "plog_blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "blog_name")
    private String blogName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "short_intro")
    private String shortIntro;

    @Column(name = "intro_html")
    private String introHtml;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    public Boolean isOwner(Long userID) {
        return this.getUser().getId().equals(userID);
    }

    public Boolean hasPosting(Posting posting) {
        return posting.getBlogID().equals(this.id);
    }

    public Boolean hasTag(Tag tag) {
        return tag.getBlogID().equals(this.id);
    }

    public Boolean isSelfSubscribe(User user) {
        return user.getId().equals(this.user.getId());
    }
}

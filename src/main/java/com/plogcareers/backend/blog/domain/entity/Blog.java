package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.GetBlogResponse;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog", schema = "plog_blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blog_name")
    private String blogName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "short_intro")
    private String shortIntro;

    @Column(name = "intro_html")
    private String introHTML;

    @Column(name = "intro_md")
    private String introMd;

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

    public GetBlogResponse toGetBlogResponse() {
        return GetBlogResponse.builder()
                .blogID(this.id)
                .blogName(this.blogName)
                .blogUser(this.user.toBlogUserDTO())
                .shortIntro(this.shortIntro)
                .introHtml(this.introHTML)
                .build();
    }
}

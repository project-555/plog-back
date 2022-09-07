package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.ums.domain.entity.User;
import javax.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name="blog", schema = "plog_blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

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

}

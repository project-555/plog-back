package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.SubscribeDTO;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "subscribe", schema = "plog_blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    public SubscribeDTO toSubscribeDTO() {
        return SubscribeDTO.builder()
                .id(this.id)
                .blogId(this.blog.getId())
                .blogName(this.blog.getBlogName())
                .user(this.blog.getUser().toSubscribeUserDTO())
                .build();
    }

    public Boolean isOwner(User user) {
        return user.getId().equals(this.userID);
    }

}

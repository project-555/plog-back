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
    private Long userId;

    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    public SubscribeDTO toSubscribeDTO(Blog blog) {
        return SubscribeDTO.builder()
                .id(this.id)
                .blog_id(blog.getId())
                .blog_name(blog.getBlogName())
                .blog_user_id(blog.getUser().getId())
                .nickname(blog.getUser().getNickname())
                .profile_image_url(blog.getUser().getProfileImageURL())
                .build();
    }

    public Boolean isOwner(User user) {
        return user.getId().equals(this.userId);
    }

}

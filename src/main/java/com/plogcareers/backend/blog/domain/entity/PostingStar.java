package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.PostingStarDTO;
import com.plogcareers.backend.ums.domain.entity.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "posting_star", schema = "plog_blog")
@NoArgsConstructor
@AllArgsConstructor
public class PostingStar {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "posting_id")
    private Long postingID;

    public PostingStarDTO toPostingStarDTO() {
        return PostingStarDTO.builder()
                .id(this.id)
                .postingID(this.postingID)
                .user(this.user.toPostingStarUserDTO())
                .build();
    }
}

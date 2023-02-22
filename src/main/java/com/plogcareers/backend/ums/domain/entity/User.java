package com.plogcareers.backend.ums.domain.entity;

import com.plogcareers.backend.blog.domain.dto.BlogUserDTO;
import com.plogcareers.backend.blog.domain.dto.HomePostingUserDTO;
import com.plogcareers.backend.blog.domain.model.CommentUserDTO;
import com.plogcareers.backend.blog.domain.model.PostingStarUserDTO;
import com.plogcareers.backend.blog.domain.model.SubscribeUserDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(schema = "plog_ums", name = "user")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "password", length = 512, nullable = false)
    private String password;

    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;

    @Column(name = "join_dt")
    private LocalDateTime joinDt;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "sex")
    private String sex;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageURL;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> roles = new ArrayList<>();

    @Override
    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        List<String> roleStrs = new ArrayList<>();
        this.roles.forEach(userRole -> {
            roleStrs.add(userRole.getRole());
        });
        return roleStrs;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roleStrs = new ArrayList<>();
        roles.forEach(userRole -> {
            roleStrs.add(userRole.getRole());
        });
        return roleStrs.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public CommentUserDTO toCommentUserDTO() {
        return CommentUserDTO.builder().userId(this.id).nickname(this.nickname).build();
    }

    public PostingStarUserDTO toPostingStarUserDTO() {
        return PostingStarUserDTO.builder()
                .id(this.id)
                .nickname(this.nickname)
                .build();
    }

    public BlogUserDTO toBlogUserDTO() {
        return BlogUserDTO.builder()
                .userID(this.id)
                .nickname(this.nickname)
                .profileImageURL(this.profileImageURL)
                .build();
    }

    public HomePostingUserDTO toHomePostingUserDTO() {
        return HomePostingUserDTO.builder()
                .userID(this.id)
                .nickname(this.nickname)
                .build();
    }

    public SubscribeUserDTO toSubscribeUserDTO() {
        return SubscribeUserDTO.builder()
                .blogUserId(this.id)
                .nickname(this.nickname)
                .profileImageUrl(this.profileImageURL)
                .build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


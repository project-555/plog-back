package com.plogcareers.backend.ums.domain.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> roles = new ArrayList<>();

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


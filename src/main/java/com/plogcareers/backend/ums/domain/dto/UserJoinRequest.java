package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


// 성별
enum Sex {
    MALE, FEMALE
}


@Getter
@NoArgsConstructor
public class UserJoinRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate birth;
    private Sex sex;

    public User toEntity(PasswordEncoder passwordEncoder) {

        return User.builder()
                .email(this.email)
                .firstName(firstName)
                .lastName(this.lastName)
                .nickname(this.nickName)
                .password(passwordEncoder.encode(this.password))
                .sex(String.valueOf(this.sex))
                .joinDt(LocalDateTime.now())
                .birth(this.birth)
                .roles(List.of(UserRole.builder().role("ROLE_USER").build()))
                .build();
    }
}

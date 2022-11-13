package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
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
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    @Length(min = 1, max = 30)
    private String firstName;
    @NotNull
    @NotBlank
    @Length(min = 1, max = 20)
    private String lastName;
    @NotNull
    @NotBlank
    @Length(min = 1, max = 30)
    private String nickName;
    @NotNull
    @NotBlank
    @Past
    private LocalDate birth;
    @NotNull
    @NotBlank
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

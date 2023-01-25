package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
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
    @ApiParam(value = "회원가입 할 이메일")
    @NotNull
    @Email
    private String email;

    @ApiParam(value = "이메일 인증 후 얻은 토큰")
    private String verifyToken;

    @NotNull
    @ApiParam(value = "회원가입 할 비밀번호")
    private String password;

    @NotNull
    @Length(min = 1, max = 30)
    @ApiParam(value = "회원가입 할 이름")
    private String firstName;

    @NotNull
    @Length(min = 1, max = 20)
    @ApiParam(value = "회원가입 할 성")
    private String lastName;

    @NotNull
    @Length(min = 1, max = 30)
    @ApiParam(value = "회원가입 할 닉네임")
    private String nickName;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiParam(value = "생년월일")
    private LocalDate birth;

    @NotNull
    @ApiParam(value = "성별", allowableValues = "FEMALE,MALE")
    private Sex sex;


    @NotNull
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "블로그 이름은 영문 소문자와 숫자로만 구성되어야 합니다. (4~20자)")
    private String blogName;
    private String shortIntro;
    private String introHtml;


    public User toUserEntity(PasswordEncoder passwordEncoder) {

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

    public Blog toBlogEntity(User user) {
        return Blog.builder()
                .user(user)
                .blogName(this.blogName)
                .shortIntro(this.shortIntro)
                .introHtml(this.introHtml)
                .build();
    }
}

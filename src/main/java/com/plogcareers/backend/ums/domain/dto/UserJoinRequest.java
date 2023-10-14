package com.plogcareers.backend.ums.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.ums.domain.entity.Sex;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.domain.entity.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinRequest {
    @ApiModelProperty(value = "회원가입 할 이메일")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(value = "이메일 인증 후 얻은 토큰")
    private String verifyToken;

    @NotNull
    @ApiModelProperty(value = "회원가입 할 비밀번호")
    private String password;

    @NotNull
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "회원가입 할 이름")
    private String firstName;

    @NotNull
    @Length(min = 1, max = 20)
    @ApiModelProperty(value = "회원가입 할 성")
    private String lastName;

    @NotNull
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "회원가입 할 닉네임")
    private String nickname;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "생년월일")
    private LocalDate birth;

    @NotNull
    @ApiModelProperty(value = "성별", allowableValues = "FEMALE,MALE")
    private Sex sex;


    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{4,20}$", message = "블로그 이름은 영문 소문자와 숫자 혹은 특수문자 (\"_\",\"-\") 만 구성되어야 합니다. (4~20자)")
    private String blogName;
    private String shortIntro;
    private String introHTML;


    public User toUserEntity(String encodedPassword) {

        return User.builder()
                .email(this.email)
                .firstName(firstName)
                .lastName(this.lastName)
                .nickname(this.nickname)
                .password(encodedPassword)
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
                .introHTML(this.introHTML)
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();
    }
}

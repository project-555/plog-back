package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Blog;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PatchBlogIntroRequest {
    @NotNull
    @ApiModelProperty(value = "정보를 수정할 블로그 ID")
    private Long blogID;

    @ApiModelProperty(value = "수정할 짧은 블로그 소개글")
    private String shortIntro;

    @ApiModelProperty(value = "블로그 소개 html")
    private String introHTML;

    @ApiModelProperty(value = "블로그 소개 markdown")
    private String introMd;

    public Boolean isValid() {
        if ((this.introHTML != null && this.introMd != null) || (this.introHTML == null && this.introMd == null)) {
            return true;
        } else {
            return false;
        }
    }

    public Blog toBlogEntity(Blog blog) {
        if (this.shortIntro != null) {
            blog.setShortIntro(this.shortIntro);
        }

        if (this.introHTML != null && this.introMd != null) {
            blog.setIntroHTML(this.introHTML);
            blog.setIntroMd(this.introMd);
        }

        return blog;
    }
}

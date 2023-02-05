package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Tag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UpdateTagRequest {
    @NotNull
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "수정할 태그 이름")
    String tagName;

    public Tag toTagEntity(Tag tag) {
        tag.setTagName(this.tagName);
        return tag;
    }
}

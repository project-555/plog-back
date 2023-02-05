package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Tag;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTagRequest {
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "생성할 태그명 (길이 1~30)")
    String tagName;

    public Tag toTagEntity(Long blogID) {
        return Tag.builder()
                .blogID(blogID)
                .tagName(this.tagName)
                .build();
    }

}

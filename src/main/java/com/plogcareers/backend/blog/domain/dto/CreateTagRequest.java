package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Tag;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTagRequest {
    @Length(min = 1, max = 30)
    String tagName;

    public Tag toTagEntity(Long blogID) {
        return Tag.builder()
                .blogID(blogID)
                .tagName(this.tagName)
                .build();
    }

}

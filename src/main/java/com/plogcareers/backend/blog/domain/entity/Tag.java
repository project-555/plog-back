package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.TagDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tag", schema = "plog_blog")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blog_id")
    private Long blogID;

    @Column(name = "tag_name")
    private String tagName;

    public TagDTO toTagDTO() {
        return TagDTO.builder()
                .tagId(this.id)
                .tagName(this.tagName)
                .build();
    }

    public PostingTag toPostingTag(Posting posting) {
        return PostingTag.builder()
                .tag(this)
                .posting(posting)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(id, tag.id) && Objects.equals(blogID, tag.blogID) && Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blogID, tagName);
    }
}

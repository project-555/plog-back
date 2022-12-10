package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.TagDTO;
import lombok.Getter;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "tag", schema = "plog_blog")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blog_id")
    private Long blogID;

    @Column(name = "tag_name")
    private String tagName;

    public TagDTO toTagDto() {
        return TagDTO.builder()
                .tagId(id)
                .tagName(tagName)
                .build();
    }

    public PostingTag toPostingTag(Posting posting) {
        return PostingTag.builder()
                .tag(this)
                .posting(posting)
                .build();
    }
}

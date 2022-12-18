package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posting_tag", schema = "plog_blog")
public class PostingTag {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    public PostingTagDTO toPostingTagDto() {
        return PostingTagDTO.builder()
                .tagId(tag.getId())
                .tagName(tag.getTagName())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostingTag that = (PostingTag) o;
        return Objects.equals(id, that.id) && Objects.equals(tag, that.tag) && Objects.equals(posting, that.posting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, posting);
    }
}

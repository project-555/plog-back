package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@Table(name = "category", schema = "plog_blog")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_desc")
    private String categoryDesc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    public CategoryDTO toCategoryDto() {
        return CategoryDTO.builder()
                .categoryId(id)
                .categoryName(categoryName)
                .categoryDesc(categoryDesc)
                .build();
    }

    public Boolean isOwner(Long userID) {
        return this.blog.getUser().getId().equals(userID);
    }
}

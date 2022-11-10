package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Getter
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

    @Column(name = "sort", nullable = false)
    private int sort;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    public CategoryDTO toCategoryDto(){
        return CategoryDTO.builder()
                .categoryId(id)
                .categoryName(categoryName)
                .sort(sort)
                .build();
    }
}

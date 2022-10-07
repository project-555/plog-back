package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.dto.PostingDetailCategoryDto;

import javax.persistence.*;

@Entity
@Table(name = "category", schema = "plog_blog")
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

    @Column(name = "blog_id")
    private Long blogId;

    public PostingDetailCategoryDto toPostingDetailCategoryDto(){
        return null;
    }
}

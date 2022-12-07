package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CategoryRepositortySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;
    private final QCategory qCategory = QCategory.category;

    public CategoryRepositortySupport(EntityManager entityManager) {
        super(Category.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Boolean isDuplicated(Long blogID, Long categoryID, String categoryName) {
        Category dupCategory = queryFactory.selectFrom(qCategory)
                .where(qCategory.blog.id.eq(blogID).and(qCategory.categoryName.eq(categoryName)))
                .fetchFirst();

        if (dupCategory == null) {
            return false;
        }

        return !dupCategory.getId().equals(categoryID);
    }

}
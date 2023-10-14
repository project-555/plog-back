package com.plogcareers.backend.blog.repository.postgres;

import com.plogcareers.backend.blog.domain.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class VPostingRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final QVPosting qPosting = QVPosting.vPosting;

    public VPostingRepositorySupport(EntityManager entityManager) {
        super(Posting.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<VPosting> listPostingsByOwner(Long blogID, String search, Long categoryID, List<PostingTag> postingTags, Long lastCursorID, Long pageSize) {
        BooleanBuilder where = new BooleanBuilder();

        where.and(qPosting.blogID.eq(blogID));
        where.and(postingTagIn(postingTags));
        where.and(categoryIDEq(categoryID));
        where.and(ltPostingID(lastCursorID));

        if (search != null && !search.trim().isEmpty()) {
            where.and(titleContains(search).or(mdContentContains(search)));
        }

        return queryFactory.selectFrom(qPosting).where(where).limit(pageSize)
                .orderBy(qPosting.id.desc())
                .fetch();
    }

    public List<VPosting> listPostingsByUserAndGuest(Long blogID, String search, Long categoryID, List<PostingTag> postingTags, Long lastCursorID, Long pageSize) {
        BooleanBuilder where = new BooleanBuilder();

        where.and(qPosting.blogID.eq(blogID));
        where.and(postingTagIn(postingTags));
        where.and(categoryIDEq(categoryID));
        where.and(ltPostingID(lastCursorID));
        where.and(qPosting.stateID.eq(State.PUBLIC));

        if (search != null && !search.trim().isEmpty()) {
            where.and(titleContains(search).or(mdContentContains(search)));
        }

        return queryFactory.selectFrom(qPosting)
                .where(where).limit(pageSize)
                .orderBy(qPosting.id.desc())
                .fetch();
    }

    public List<VPosting> listHomePostings(String search, Long lastCursorID, Long pageSize) {
        BooleanBuilder where = new BooleanBuilder();

        where.and(ltPostingID(lastCursorID));
        where.and(qPosting.stateID.eq(State.PUBLIC));

        if (search != null && !search.trim().isEmpty()) {
            where.and(titleContains(search).or(mdContentContains(search)));
        }

        return queryFactory.selectFrom(qPosting)
                .where(where)
                .orderBy(qPosting.id.desc())
                .limit(pageSize)
                .fetch();
    }

    public Long countPostings(Long blogID, String search, List<Long> categoryIDs) {
        BooleanBuilder where = new BooleanBuilder();


        where.and(qPosting.blogID.eq(blogID));
        if (categoryIDs != null && !categoryIDs.isEmpty())
            where.and(categoryIDIn(categoryIDs));
        if (search != null && !search.trim().isEmpty())
            where.and(titleContains(search).or(mdContentContains(search)));


        return queryFactory.select(qPosting.count())
                .from(qPosting)
                .where(where)
                .fetchOne();
    }


    private BooleanExpression ltPostingID(Long cursorID) {
        if (cursorID == null) {
            return null;
        }
        return qPosting.id.lt(cursorID);
    }

    private BooleanExpression postingTagIn(List<PostingTag> postingTags) {
        if (postingTags == null || postingTags.isEmpty()) {
            return null;
        }
        List<Long> postingIDs = postingTags.stream().map(postingTag -> postingTag.getPosting().getId()).toList();
        return qPosting.id.in(postingIDs);
    }

    private BooleanExpression titleContains(String title) {
        return qPosting.title.upper().contains(title.toUpperCase());
    }

    private BooleanExpression mdContentContains(String mdContent) {
        return qPosting.mdContent.upper().contains(mdContent.toUpperCase());
    }

    private BooleanExpression categoryIDEq(Long categoryID) {
        return categoryID != null ? qPosting.categoryID.eq(categoryID) : null;
    }

    private BooleanExpression categoryIDIn(List<Long> categoryIDs) {
        return categoryIDs != null && !categoryIDs.isEmpty() ? qPosting.categoryID.in(categoryIDs) : null;
    }

}

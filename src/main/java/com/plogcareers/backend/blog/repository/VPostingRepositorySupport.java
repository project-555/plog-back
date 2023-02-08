package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    public List<VPosting> listPostingsByOwner(Long blogID, String search, Long categoryID, List<PostingTag> postingTags) {
        return queryFactory.selectFrom(qPosting).where(
                qPosting.blogID.eq(blogID),
                titleContains(search),
                mdContentContains(search),
                postingTagIn(postingTags),
                categoryIDEq(categoryID)
        ).fetch();
    }

    public List<VPosting> listPostingsByUserAndGuest(Long blogID, String search, Long categoryID, List<PostingTag> postingTags) {
        return queryFactory.selectFrom(qPosting)
                .where(
                        qPosting.blogID.eq(blogID),
                        titleContains(search),
                        mdContentContains(search),
                        postingTagIn(postingTags),
                        categoryIDEq(categoryID),
                        qPosting.stateID.eq(State.PUBLIC)
                )
                .fetch();
    }

    public List<VPosting> listHomePostings(Long lastCursorID, Long pageSize) {
        return queryFactory.selectFrom(
                        qPosting
                )
                .where(
                        ltPostingID(lastCursorID),
                        qPosting.stateID.eq(State.PUBLIC)

                )
                .orderBy(qPosting.id.desc())
                .limit(pageSize)
                .fetch();
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

        return title != null ? qPosting.title.contains(title) : null;
    }

    private BooleanExpression mdContentContains(String mdContent) {
        return mdContent != null ? qPosting.mdContent.contains(mdContent) : null;
    }

    private BooleanExpression categoryIDEq(Long categoryID) {
        return categoryID != null ? qPosting.categoryID.eq(categoryID) : null;
    }
}

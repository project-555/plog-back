package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingTag;
import com.plogcareers.backend.blog.domain.entity.QPosting;
import com.plogcareers.backend.blog.domain.entity.State;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PostingRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final QPosting qPosting = QPosting.posting;

    public PostingRepositorySupport(EntityManager entityManager) {
        super(Posting.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Posting> listPostingsByOwner(Long blogID, String search, Long categoryID, List<PostingTag> postingTags) {
        return queryFactory.selectFrom(qPosting).where(
                qPosting.blogID.eq(blogID),
                titleContains(search),
                mdContentContains(search),
                postingTagIn(postingTags),
                categoryIDEq(categoryID)
        ).fetch();
    }

    public List<Posting> listPostingsByUserAndGuest(Long blogID, String search, Long categoryID, List<PostingTag> postingTags) {
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

package com.plogcareers.backend.blog.repository;

import com.plogcareers.backend.blog.domain.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class VHotPostingRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final QVHotPosting qHotPosting = QVHotPosting.vHotPosting;

    public VHotPostingRepositorySupport(EntityManager entityManager) {
        super(Posting.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<VHotPosting> listFollowingHomePostings(Long lastPostingID, List<Long> followingIDs, int pageSize) {
        return queryFactory.selectFrom(
                        qHotPosting
                )
                .where(
                        ltPostingID(lastPostingID),
                        qHotPosting.stateID.eq(State.PUBLIC),
                        qHotPosting.blogID.in(followingIDs)
                )
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression ltPostingID(Long postingID) {
        if (postingID == null) {
            return null;
        }
        return qHotPosting.id.lt(postingID);
    }
}
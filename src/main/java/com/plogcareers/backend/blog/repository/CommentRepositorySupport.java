package com.plogcareers.backend.blog.repository;


import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CommentRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public CommentRepositorySupport(EntityManager entityManager) {
        super(Comment.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Comment> ListComments(Long postingId) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(comment.postingId.eq(postingId))
                .orderBy(comment.parentCommentId.asc().nullsFirst())
                .fetch();
    }
}

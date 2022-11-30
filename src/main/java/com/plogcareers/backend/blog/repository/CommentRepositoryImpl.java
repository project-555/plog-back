package com.plogcareers.backend.blog.repository;


import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.entity.QComment;
import com.plogcareers.backend.ums.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<Comment> findByUserAndGuest(Long postingId, User user, Pageable pageable) {
        QComment comment = QComment.comment;

        return queryFactory
                .selectFrom(comment)
                .where(
                        comment.postingId.eq(postingId).and(
                                comment.isSecret.isFalse().or(comment.isSecret.isTrue().and(comment.user.eq(user)))
                        )
                )
                .orderBy(comment.parentCommentId.asc().nullsFirst())
                .fetch();

    }


    @Override
    public List<Comment> findByBlogOwner(Long postingId, Pageable pageable) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(comment.postingId.eq(postingId))
                .orderBy(comment.parentCommentId.asc().nullsFirst())
                .fetch();
    }
}

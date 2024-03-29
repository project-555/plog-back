package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.postgres.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.postgres.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final BlogRepository blogRepository;
    private final PostingRepository postingRepository;
    private final VPostingRepositorySupport postingRepositorySupport;
    private final PostingTagRepository postingTagRepository;
    private final TagRepository tagRepository;
    private final StateRepository stateRepository;
    private final CommentRepository commentRepository;
    private final CommentRepositorySupport commentRepositorySupport;
    private final UserRepository userRepository;
    private final PostingStarRepository postingStarRepository;
    private final CategoryRepository categoryRepository;

    // 글 저장
    public CreatePostingResponse createPosting(Long blogID, Long userID, CreatePostingRequest request) throws TagNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(userID)) {
            throw new NotProperAuthorityException();
        }
        if (!categoryRepository.existsById(request.getCategoryID())) {
            throw new CategoryNotFoundException();
        }

        Posting posting = postingRepository.save(request.toEntity(blogID, userID));

        if (request.getTagIDs() != null && !request.getTagIDs().isEmpty()) {
            List<Tag> tags = tagRepository.findByIdIn(request.getTagIDs());
            postingTagRepository.saveAll(tags.stream().map(tag -> tag.toPostingTag(posting)).toList());
        }

        return CreatePostingResponse.builder()
                .postingID(posting.getId())
                .build();
    }

    // 글 가져오기
    public GetPostingResponse getPosting(Long blogID, Long postingID, Long loginedUserID) throws PostingNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        if (!blog.isOwner(loginedUserID) && !posting.getStateID().equals(State.PUBLIC)) {
            throw new PostingNotFoundException();
        }

        posting.setHitCnt(posting.getHitCnt() + 1L);
        postingRepository.save(posting);

        return posting.toGetPostingResponse();
    }

    public ListPostingsResponse listPostings(Long blogID, Long loginedUserID, ListPostingsRequest request) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        List<PostingTag> searchTags = null;
        if (request.getTagIDs() != null && !request.getTagIDs().isEmpty()) {
            searchTags = postingTagRepository.findByTag_IdIn(request.getTagIDs());
        }
        List<VPosting> postings;

        if (blog.isOwner(loginedUserID)) {
            postings = postingRepositorySupport.listPostingsByOwner(blogID, request.getSearch(), request.getCategoryID(), searchTags, request.getLastCursorID(), request.getPageSize());
        } else {
            postings = postingRepositorySupport.listPostingsByUserAndGuest(blogID, request.getSearch(), request.getCategoryID(), searchTags, request.getLastCursorID(), request.getPageSize());
        }

        List<Long> postingIDs = postings.stream().map(VPosting::getId).toList();
        List<PostingTag> postingTags = postingTagRepository.findByPostingIdIn(postingIDs);
        HashMap<Long, List<PostingTag>> postingTagMap = new HashMap<>();

        for (PostingTag postingTag : postingTags) {
            if (postingTagMap.containsKey(postingTag.getPosting().getId())) {
                postingTagMap.get(postingTag.getPosting().getId()).add(postingTag);
            } else {
                postingTagMap.put(postingTag.getPosting().getId(), new ArrayList<>(List.of(postingTag)));
            }
        }

        return new ListPostingsResponse(postings.stream().map(
                (VPosting posting) -> posting.toPostingDTO(postingTagMap.get(posting.getId()))
        ).toList());
    }

    public CountPostingsResponse countPostings(CountPostingsRequest request) throws BlogNotFoundException {
        if (!blogRepository.existsById(request.getBlogID())) {
            throw new BlogNotFoundException();
        }

        Long count = postingRepositorySupport.countPostings(request.getBlogID(), request.getSearch(), request.getCategoryIDs());

        return new CountPostingsResponse(count);
    }

    // 포스팅 태그 가져오기
    public ListPostingTagResponse listPostingTags(Long blogID, Long postingID) throws PostingNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }

        List<PostingTag> postingTags = postingTagRepository.findByPostingId(posting.getId());

        return new ListPostingTagResponse(postingTags.stream().map(PostingTag::toPostingTagDto).toList());
    }

    // 글 수정하기
    @Transactional
    public void updatePosting(Long loginedUserID, Long blogID, Long postingID, @NotNull UpdatePostingRequest request) throws PostingNotFoundException, CategoryNotFoundException, NotProperAuthorityException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        if (!categoryRepository.existsById(request.getCategoryID())) {
            throw new CategoryNotFoundException();
        }

        postingTagRepository.deleteAllByPosting(posting);

        postingRepository.save(request.toPostingEntity(posting));

        if (request.getTagIDs() != null && !request.getTagIDs().isEmpty()) {
            List<Tag> tags = tagRepository.findByIdIn(request.getTagIDs());
            postingTagRepository.saveAll(tags.stream().map(tag -> tag.toPostingTag(posting)).toList());
        }
    }

    // 글 삭제
    public void deletePosting(Long blogID, Long postingID, Long userID) throws PostingNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(userID)) {
            throw new NotProperAuthorityException();
        }
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        postingRepository.deleteById(posting.getId());
    }

    public ListStateResponse listStates() {
        List<State> states = stateRepository.findAll();
        return new ListStateResponse(states.stream().map(State::toStateDTO).toList());
    }


    public void createComment(Long blogID, Long postingID, Long loginedUserID, CreateCommentRequest request) throws UserNotFoundException, PostingNotFoundException, InvalidParentExistException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);

        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }

        if (request.getParentCommentID() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentID()).orElseThrow(ParentCommentNotFoundException::new);
            if (parentComment.getParentCommentID() != null) {
                throw new InvalidParentExistException();
            }
        }

        commentRepository.save(request.toCommentEntity(
                postingID,
                userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new)
        ));
    }

    public void updateComment(Long blogID, Long postingID, Long commentID, Long loginedUserID, UpdateCommentRequest request) throws NotProperAuthorityException, PostingNotFoundException, CommentNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);

        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }

        Comment comment = commentRepository.findById(commentID).orElseThrow(CommentNotFoundException::new);
        if (!comment.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        if (!posting.hasComment(comment)) {
            throw new PostingCommentUnmatchedException();
        }

        commentRepository.save(request.toCommentEntity(comment));
    }

    public void deleteComment(Long blogID, Long postingID, Long commentID, Long loginedUserID) throws PostingNotFoundException, CommentNotFoundException, NotProperAuthorityException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        Comment comment = commentRepository.findById(commentID).orElseThrow(CommentNotFoundException::new);
        if (!posting.hasComment(comment)) {
            throw new PostingCommentUnmatchedException();
        }

        if (!posting.isOwner(loginedUserID) && !comment.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        commentRepository.delete(comment);
    }


    public ListCommentsResponse listComments(Long blogID, Long postingID, Long loginedUserID) throws PostingNotFoundException, UserNotFoundException {
        // 블로그 존재 확인
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);

        // 포스팅 존재 확인 및 해당 블로그에 포함되어 있는지 확인
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }

        return new ListCommentsResponse(commentRepositorySupport.ListComments(postingID), posting.isOwner(loginedUserID), loginedUserID);
    }

    public ListPostingStarsResponse listPostingStars(Long blogID, Long postingID) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        List<PostingStar> postingStars = postingStarRepository.findByPostingID(postingID);
        return new ListPostingStarsResponse(postingStars.stream().map(PostingStar::toPostingStarDTO).toList());
    }

    public void createPostingStar(Long blogID, Long postingID, Long loginedUserID) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        User user = userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        if (postingStarRepository.existsByPostingIDAndUserId(postingID, loginedUserID)) {
            throw new PostingStarDuplicatedException();
        }

        postingStarRepository.save(PostingStar.builder().user(user).postingID(postingID).build());
    }

    public void deletePostingStar(Long blogID, Long postingID, Long loginedUserID) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }
        PostingStar postingStar = postingStarRepository.findFirstByPostingIDAndUserId(postingID, loginedUserID).orElseThrow(PostingStarNotFoundException::new);

        postingStarRepository.delete(postingStar);
    }
}


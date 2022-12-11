package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Long createPosting(Long blogID, Long userID, CreatePostingRequest request) throws TagNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(userID)) {
            throw new NotProperAuthorityException();
        }
        if (!categoryRepository.existsById(request.getCategoryID())) {
            throw new CategoryNotFoundException();
        }

        Posting posting = postingRepository.save(request.toEntity());

        if (request.getTagIDs() != null && !request.getTagIDs().isEmpty()) {
            List<Tag> tags = tagRepository.findByIdIn(request.getTagIDs());
            postingTagRepository.saveAll(tags.stream().map(tag -> tag.toPostingTag(posting)).toList());
        }

        return posting.getId();
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

        return posting.toGetPostingResponse();
    }

    public ListPostingsResponse listPostings(Long blogID, Long loginedUserID, ListPostingsRequest request) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        List<PostingTag> postingTags = null;
        if (request.getTagIDs() != null && !request.getTagIDs().isEmpty()) {
            postingTags = postingTagRepository.findByTag_IdIn(request.getTagIDs());
        }
        List<VPosting> postings;
        if (blog.isOwner(loginedUserID)) {
            postings = postingRepositorySupport.listPostingsByOwner(blogID, request.getSearch(), request.getCategoryID(), postingTags);
        } else {
            postings = postingRepositorySupport.listPostingsByUserAndGuest(blogID, request.getSearch(), request.getCategoryID(), postingTags);
        }
        return new ListPostingsResponse(postings.stream().map(VPosting::toPostingDTO).toList());
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
    public void updatePosting(Long loginedUserID, Long postingID, @NotNull UpdatePostingRequest request) throws PostingNotFoundException, NotProperAuthorityException {
        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        Blog blog = blogRepository.findById(posting.getBlogId()).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        postingRepository.save(request.toPostingEntity(posting));
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

        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
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
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        Posting posting = postingRepository.findById(postingID).orElseThrow(PostingNotFoundException::new);
        if (!blog.hasPosting(posting)) {
            throw new BlogPostingUnmatchedException();
        }

        Comment comment = commentRepository.findById(commentID).orElseThrow(CommentNotFoundException::new);
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
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
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


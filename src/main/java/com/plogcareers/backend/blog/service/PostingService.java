package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingTag;
import com.plogcareers.backend.blog.domain.entity.State;
import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import com.plogcareers.backend.blog.domain.model.StateDTO;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.*;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final BlogRepository blogRepository;
    private final PostingRepository postingRepository;
    private final CategoryRepository categoryRepository;
    private final PostingTagRepository postingTagRepository;
    private final TagRepository tagRepository;
    private final StateRepository stateRepository;
    private final CommentRepository commentRepository;
    private final CommentRepositorySupport commentRepositorySupport;
    private final UserRepository userRepository;

    // 글 저장
    public Long createPosting(@NotNull CreatePostingRequest createPostingRequest) throws TagNotFoundException {
        Posting posting = postingRepository.save(createPostingRequest.toEntity());
        for (Long tagId : createPostingRequest.getTagIds()) {
            postingTagRepository.save(
                    PostingTag.builder()
                            .posting(posting)
                            .tag(tagRepository.findById(tagId).orElseThrow(TagNotFoundException::new))
                            .build()
            );
        }

        return posting.getId();
    }

    // 글 가져오기
    public GetPostingResponse getPosting(Long postingId) throws PostingNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        return GetPostingResponse.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .htmlContent(posting.getHtmlContent())
                .stateId(posting.getStateId())
                .updateDt(posting.getUpdateDt())
                .isCommentAllowed(posting.isCommentAllowed())
                .thumbnailImageUrl(posting.getThumbnailImageUrl())
                .mdContent(posting.getMdContent())
                .build();
    }

    // 포스팅 태그 가져오기
    public ListPostingTagResponse listPostingTag(Long postingId) throws PostingNotFoundException {
        if (!postingRepository.existsById(postingId)) {
            throw new PostingNotFoundException();
        }
        List<PostingTagDTO> postingTagList = postingTagRepository.findPostingTagsByPostingId(postingId)
                .stream()
                .map(PostingTag::toPostingTagDto)
                .toList();
        return ListPostingTagResponse.builder()
                .postingTags(postingTagList)
                .build();
    }

    // 글 삭제
    public void deletePosting(@NotNull Long postingId) throws PostingNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        postingRepository.deleteById(posting.getId());
    }

    public List<StateDTO> listStates() {
        List<State> states = stateRepository.findAll();
        return states.stream()
                .map(state -> new StateDTO(state.getId(), state.getStateName()))
                .toList();
    }


    public void createComment(CreateCommentRequest request, Long postingId, Long loginedUserID) throws UserNotFoundException, PostingNotFoundException, InvalidParentExistException {
        if (!postingRepository.existsById(postingId)) {
            throw new PostingNotFoundException();
        }
        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId()).orElseThrow(ParentCommentNotFoundException::new);
            if (parentComment.getParentCommentId() != null) {
                throw new InvalidParentExistException();
            }
        }

        commentRepository.save(request.toCommentEntity(
                postingId,
                userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new)
        ));
    }

    public void updateComment(UpdateCommentRequest request, Long postingId, Long commentId, Long loginedUserId) throws NotProperAuthorityException, PostingNotFoundException, CommentNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!comment.getUser().getId().equals(loginedUserId)) {
            throw new NotProperAuthorityException();
        }
        if (!comment.getPostingId().equals(posting.getId())) {
            throw new CommentPostingMismatchedException();
        }
        commentRepository.save(request.toCommentEntity(comment));
    }

    public void deleteComment(Long postingId, Long commentId, Long loginedUserId) throws PostingNotFoundException, CommentNotFoundException, NotProperAuthorityException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!posting.isOwner(loginedUserId) && !comment.isOwner(loginedUserId)) {
            throw new NotProperAuthorityException();
        }
        if (!comment.getPostingId().equals(posting.getId())) {
            throw new CommentPostingMismatchedException();
        }
        commentRepository.delete(comment);
    }


    public ListCommentsResponse listComments(Long loginedUserId, Long postingId) throws PostingNotFoundException, UserNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        return new ListCommentsResponse(commentRepositorySupport.ListComments(postingId), posting.isOwner(loginedUserId), loginedUserId);
    }
}

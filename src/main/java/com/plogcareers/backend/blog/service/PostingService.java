package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreatePostingRequest;
import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import com.plogcareers.backend.blog.domain.dto.ListCommentsResponse;
import com.plogcareers.backend.blog.domain.dto.ListPostingTagResponse;
import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingTag;
import com.plogcareers.backend.blog.domain.entity.State;
import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import com.plogcareers.backend.blog.domain.model.StateDTO;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.repository.*;
import com.plogcareers.backend.ums.domain.entity.User;
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


    public ListCommentsResponse listComments(Long loginedUserId, Long postingId) throws PostingNotFoundException, UserNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);

        List<Comment> comments;
        if (posting.isOwner(loginedUserId)) {
            comments = commentRepository.findByBlogOwner(postingId);
        } else {
            User user = userRepository.findById(loginedUserId).orElseThrow(UserNotFoundException::new);
            comments = commentRepository.findByUserAndGuest(postingId, user);
        }

        return new ListCommentsResponse(comments);
    }

}

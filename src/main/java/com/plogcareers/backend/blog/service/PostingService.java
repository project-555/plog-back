package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.*;
import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import com.plogcareers.backend.blog.domain.model.CommentDTO;
import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import com.plogcareers.backend.blog.domain.model.StateDTO;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.exception.TagNotFoundException;
import com.plogcareers.backend.blog.repository.*;
import com.plogcareers.backend.common.domain.dto.OPagingRequest;
import com.plogcareers.backend.common.domain.dto.SOPagingResponse;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

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
    public GetPostingResponse getPosting(Long postingId) throws PostingNotFoundException, UserNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        // TODO: User Vaildation 추가
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

    // 카테고리 가져오기
    public ListCategoryResponse listCategory(Long blogId) throws BlogNotFoundException {
        if (blogRepository.existsById(blogId)) {
            List<CategoryDTO> categoryList = categoryRepository.findCategoryByBlogIdOrderBySort(blogId)
                    .stream()
                    .map(Category::toCategoryDto)
                    .toList();
            return ListCategoryResponse.builder()
                    .category(categoryList)
                    .build();
        }
        throw new BlogNotFoundException();
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


    public SOPagingResponse<List<CommentDTO>> listComments(Long loginedUserId, Long postingId, OPagingRequest request) throws PostingNotFoundException, UserNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        Page<Comment> comments;
        if (Objects.equals(posting.getUserId(), loginedUserId)) {
            comments = commentRepository.findByPostingIdAndParentIsNullOrderByUpdateDtDesc(
                    postingId,
                    PageRequest.of(request.getPage() - 1, request.getPageSize()));
        } else {
            comments = commentRepository.findByPostingIdAndParentIsNullAndUserAndIsSecretTrueOrIsSecretFalseOrderByUpdateDtDesc(
                    postingId,
                    userRepository.findById(loginedUserId).orElseThrow(UserNotFoundException::new),
                    PageRequest.of(request.getPage() - 1, request.getPageSize()));
        }
        return new ListCommentsResponse(comments.stream().map(Comment::toCommentDTO).toList())
                .toOPagingResponse(
                        request.getPage(),
                        request.getPageSize(),
                        comments.getTotalElements()
                );

    }

}

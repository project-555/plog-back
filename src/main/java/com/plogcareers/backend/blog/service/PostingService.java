package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreatePostingRequest;
import com.plogcareers.backend.blog.domain.dto.GetCategoryResponse;
import com.plogcareers.backend.blog.domain.dto.GetPostingResponse;
import com.plogcareers.backend.blog.domain.dto.ListPostingTagResponse;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.entity.PostingTag;
import com.plogcareers.backend.blog.domain.entity.State;
import com.plogcareers.backend.blog.domain.model.PostingTagDTO;
import com.plogcareers.backend.blog.domain.model.StateDTO;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import com.plogcareers.backend.blog.repository.PostingRepository;
import com.plogcareers.backend.blog.repository.PostingTagRepository;
import com.plogcareers.backend.blog.repository.StateRepository;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final CategoryRepository categoryRepository;
    private final PostingTagRepository postingTagRepository;
    private final UserRepository userRepository;
    private final StateRepository stateRepository;
    // 글 저장
    @Transactional
    public Long createPosting(@NotNull CreatePostingRequest createPostingRequest) {
        Posting posting = postingRepository.save(createPostingRequest.toEntity());
        return posting.getId();
    }

    // 글 가져오기
    @Transactional
    public GetPostingResponse getPosting(Long postingId) throws PostingNotFoundException, UserNotFoundException {
        Posting posting = postingRepository.findById(postingId).orElseThrow(PostingNotFoundException::new);
        User user = userRepository.findById(posting.getUserId()).orElseThrow(UserNotFoundException::new);
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
    @Transactional
    public GetCategoryResponse getCategory(Long postingId) {
        Category category = categoryRepository.findById(postingId).orElseThrow(RuntimeException::new);
        return GetCategoryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
    // 포스팅 태그 가져오기
    @Transactional
    public ListPostingTagResponse listPostingTag(Long postingId) throws PostingNotFoundException {
        ArrayList<PostingTagDTO> postingTagList = postingTagRepository.findPostingTagsByPostingId(postingId)
                .stream()
                .map(PostingTag::toPostingTagDto)
                .collect(Collectors.toCollection(ArrayList::new));
        return ListPostingTagResponse.builder()
                .postingTags(postingTagList)
                .build();
    }

    // 글 삭제
    @Transactional
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
}

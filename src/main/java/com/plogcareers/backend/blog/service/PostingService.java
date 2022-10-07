package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.PostingDetailResponse;
import com.plogcareers.backend.blog.domain.dto.PostingUpdateRequest;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Comment;
import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.dto.PostingDetailRequest;
import com.plogcareers.backend.blog.domain.entity.Tag;
import com.plogcareers.backend.blog.exception.PostingNotFoundException;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import com.plogcareers.backend.blog.repository.CommentRepository;
import com.plogcareers.backend.blog.repository.PostingRepository;
import com.plogcareers.backend.blog.repository.TagRepository;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    // 글 저장
    @Transactional
    public Long savePost(@NotNull PostingDetailRequest postingRequest) {
        User postingUser = userRepository.findById(postingRequest.getUserId()).orElseThrow(RuntimeException::new);
        Category category = categoryRepository.findById(postingRequest.getId()).orElseThrow(RuntimeException::new);
        Posting posting = postingRepository.save(postingRequest.toEntity(postingUser, category));
        return posting.getId();
    }

    // 글 가져오기
    @Transactional
    public PostingDetailResponse getPost(Long id) throws PostingNotFoundException {
        Posting posting = postingRepository.findById(id).orElseThrow(PostingNotFoundException::new);
//        // 덧글 가져오기
//        Comment comment = commentRepository.findById(id);
//        // 카테고리 가져오기
        Category category = categoryRepository.findById(id).get();
//        // 태그 가져오기
//        Tag tag = tagRepository.findById(id);

        return PostingDetailResponse.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .htmlContent(posting.getHtmlContent())
                .category(category.toPostingDetailCategoryDto())
                .stateId(posting.getStateId())
                .updateDt(posting.getUpdateDt())
                .isCommentAllowed(posting.isCommentAllowed())
                .thumbnailImageUrl(posting.getThumbnailImageUrl())
                .mdContent(posting.getMdContent())
                .build();
    }

//    @Transactional
//    public void updatePost(Long id, PostingUpdateRequest postingUpdateRequest){
//       var PostingE = postingUpdateRequest.toPostingEntity(id);
//        Posting posting = postingRepository.save(PostingE);
//    }

    // 글 삭제
    @Transactional
    public void deletePost(@NotNull Long id) {
        postingRepository.deleteById(id);
    }
}

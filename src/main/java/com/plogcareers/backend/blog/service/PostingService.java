package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.dto.PostingRequest;
import com.plogcareers.backend.blog.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    // 글 저장
    @Transactional
    public void savePost(@NotNull PostingRequest postingRequest) {
        Posting posting = postingRepository.save(postingRequest.toEntity());
        System.out.println(posting);
    }

    // 글 가져오기
    @Transactional
    public PostingRequest getPost(Long id) {
        Posting posting = postingRepository.findById(id).get();
        PostingRequest postingDto = PostingRequest.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .htmlContent(posting.getHtmlContent())
                .categoryId(posting.getCategoryId())
                .stateId(posting.getStateId())
                .updateDt(posting.getUpdateDt())
                .isCommentAllowed(posting.isCommentAllowed())
                .thumbnailImageUrl(posting.getThumbnailImageUrl())
                .mdContent(posting.getMdContent())
                .build();
        return postingDto;
    }

    // 글 삭제
    @Transactional
    public void deletePost(Long id) {
        postingRepository.deleteById(id);
    }
}

package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.PostingDeleteRequest;
import com.plogcareers.backend.blog.domain.dto.PostingResponse;
import com.plogcareers.backend.blog.domain.dto.PostingUpdateRequest;
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
    public PostingResponse getPost(Long id) {
        Posting posting = postingRepository.findById(id).get();
        PostingResponse postingResponse = PostingResponse.builder()
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
        System.out.println(postingResponse);
        return postingResponse;
    }

//    @Transactional
//    public void updatePost(Long id, PostingUpdateRequest postingUpdateRequest){
//        Posting posting = postingRepository.findById(id).get();
//        posting.setTitle(postingUpdateRequest.getTitle());
//        posting.setHtmlContent(postingUpdateRequest.getHtmlContent());
//        posting.setCategoryId(postingUpdateRequest.getCategoryId());
//        posting.setStateId(postingUpdateRequest.getStateId());
//        posting.setThumbnailImageUrl(postingUpdateRequest.getThumbnailImageUrl());
//        posting.setMdContent(postingUpdateRequest.getMdContent());
//        posting = postingRepository.save(postingUpdateRequest.toEntity());
//    }

    // 글 삭제
    @Transactional
    public void deletePost(@NotNull PostingDeleteRequest postingDeleteRequest) {
        postingRepository.deleteById(postingDeleteRequest.toEntity().getId());
    }
}

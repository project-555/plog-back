package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.entity.Posting;
import com.plogcareers.backend.blog.domain.dto.PostingDto;
import com.plogcareers.backend.blog.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostingService {

    private PostingRepository postingRepository;

    // 글 저장
    @Transactional
    public Long savePost(@NotNull PostingDto postingDto) {
        return postingRepository.save(postingDto.toEntity()).getId();
    }

    // 글 가져오기
    @Transactional
    public PostingDto getPost(Long id) {
        Posting posting = postingRepository.findById(id).get();
        PostingDto postingDto = PostingDto.builder()
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

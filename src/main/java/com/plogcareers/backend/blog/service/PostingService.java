package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.PostingDto;
import com.plogcareers.backend.blog.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostingService {

    private PostingRepository postingRepository;

    @Transactional
    public Long savePost(PostingDto postingDto) {
        return postingDto.ToString().getId();
    }

}

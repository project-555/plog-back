package com.plogcareers.backend.post.service;

import com.plogcareers.backend.post.dto.PostDto;
import com.plogcareers.backend.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private PostRepository postRepository;

    public PostService(PostService postService) {
        this.postRepository = postService.postRepository;
    }

    @Transactional
    public Long savePost(PostDto postDto) {
        return postRepository.save(postDto.toEntity()).getId();
    }
}

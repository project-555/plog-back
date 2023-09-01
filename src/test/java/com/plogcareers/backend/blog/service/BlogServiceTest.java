package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.PatchBlogRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogRepository blogRepository;

    @InjectMocks
    BlogService blogService;


    @Test
    @DisplayName("patchBlog - 블로그가 없는 경우 테스트")
    void testPatchBlog_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(Optional.empty());
        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.patchBlog(1L, 1L, PatchBlogRequest.builder().build()));
    }

    @Test
    @DisplayName("patchBlog - 로그인한 유저가 블로그 주인이 아닌 경우 테스트")
    void testPatchBlog_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.patchBlog(-1L, 1L, PatchBlogRequest.builder().build()));
    }

    @Test
    @DisplayName("patchBlog - 정상동작")
    void testPatchBlog_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        // when
        blogService.patchBlog(1L, 1L, PatchBlogRequest.builder().build());

        // then
        verify(blogRepository, times(1)).save(any());
    }
}
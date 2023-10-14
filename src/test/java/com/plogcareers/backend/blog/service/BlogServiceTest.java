package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Tag;
import com.plogcareers.backend.blog.domain.model.CategoryDTO;
import com.plogcareers.backend.blog.domain.model.TagDTO;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.postgres.BlogRepository;
import com.plogcareers.backend.blog.repository.postgres.CategoryRepository;
import com.plogcareers.backend.blog.repository.postgres.CategoryRepositorySupport;
import com.plogcareers.backend.blog.repository.postgres.TagRepository;
import com.plogcareers.backend.ums.domain.dto.CheckBlogNameExistRequest;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.BlogNameDuplicatedException;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogRepository blogRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryRepositorySupport categoryRepositorySupport;

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

    @Test
    @DisplayName("createCategory - 블로그가 없음")
    void createCategory_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.createCategory(1L, 1L,
                CreateCategoryRequest.builder()
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("createCategory - 블로그 주인이 아님")
    void createCategory_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.createCategory(1L, -1L,
                CreateCategoryRequest.builder()
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("createCategory - 카테고리 중복")
    void createCategory_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.existsByBlogAndCategoryName(Blog.builder().id(1L).user(User.builder().id(1L).build()).build(), "test_category")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(CategoryDuplicatedException.class, () -> blogService.createCategory(1L, 1L,
                CreateCategoryRequest.builder()
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("createCategory - 정상동작")
    void createCategory_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.existsByBlogAndCategoryName(Blog.builder().id(1L).user(User.builder().id(1L).build()).build(), "test_category")
        ).thenReturn(false);

        // when
        blogService.createCategory(1L, 1L,
                CreateCategoryRequest.builder()
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build());

        // then
        verify(categoryRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("listCategories - 블로그가 없음")
    void listCategories_1() {
        // given
        when(
                blogRepository.existsById(1L)
        ).thenReturn(false);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.listCategories(1L));
    }

    @Test
    @DisplayName("listCategories - 정상동작")
    void listCategories_2() {
        // given
        when(
                blogRepository.existsById(1L)
        ).thenReturn(true);
        when(
                categoryRepository.findCategoryByBlogIdOrderByCategoryName(1L)
        ).thenReturn(
                List.of(
                        Category.builder()
                                .blog(Blog.builder().id(1L).build())
                                .id(1L)
                                .categoryName("test_category_1")
                                .categoryDesc("test_category_desc_1")
                                .build(),
                        Category.builder()
                                .blog(Blog.builder().id(1L).build())
                                .id(2L)
                                .categoryName("test_category_2")
                                .categoryDesc("test_category_desc_2")
                                .build()
                )
        );

        // when
        ListCategoriesResponse got = blogService.listCategories(1L);

        // then
        ListCategoriesResponse want = ListCategoriesResponse.builder()
                .categories(
                        List.of(
                                CategoryDTO.builder()
                                        .categoryID(1L)
                                        .categoryName("test_category_1")
                                        .categoryDesc("test_category_desc_1")
                                        .build(),
                                CategoryDTO.builder()
                                        .categoryID(2L)
                                        .categoryName("test_category_2")
                                        .categoryDesc("test_category_desc_2")
                                        .build()
                        )
                )
                .build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("patchCategory - 블로그가 없음")
    void patchCategory() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("patchCategory - 카테고리가 없음")
    void patchCategory_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.findById(1L)
        ).thenThrow(CategoryNotFoundException.class);

        // when + then
        Assertions.assertThrows(CategoryNotFoundException.class, () -> blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .loginedUserID(1L)
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("patchCategory - 블로그 주인이 아님")
    void patchCategory_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .loginedUserID(-1L)
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("patchCategory - 카테고리가 블로그와 매칭되지 않음")
    void patchCategory_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.findById(2L)
        ).thenReturn(
                Optional.of(Category.builder()
                        .id(2L)
                        .blog(Blog.builder()
                                .id(2L)
                                .user(User.builder().id(2L).build())
                                .build())
                        .build()
                )
        );

        // when + then
        Assertions.assertThrows(CategoryBlogMismatchedException.class, () -> blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(2L)
                        .loginedUserID(1L)
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("patchCategory - 수정할 카테고리 명이 기존 카테고리와 중복됨")
    void patchCategory_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                categoryRepository.findById(1L)
        ).thenReturn(
                Optional.of(Category.builder()
                        .id(1L)
                        .blog(Blog.builder().id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                        )
                        .build())
        );
        when(
                categoryRepositorySupport.existsDuplicatedCategory(1L, 1L, "test_category")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(CategoryDuplicatedException.class, () -> blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .loginedUserID(1L)
                        .categoryName("test_category")
                        .categoryDesc("test_category_desc")
                        .build()));
    }

    @Test
    @DisplayName("patchCategory - 정상동작 (카테고리명만 수정)")
    void patchCategory_6() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Category.builder()
                                .id(1L)
                                .categoryName("test_category")
                                .categoryDesc("test_category_desc")
                                .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                                .build()
                )
        );
        when(
                categoryRepositorySupport.existsDuplicatedCategory(1L, 1L, "modified_category")
        ).thenReturn(false);


        // when
        blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .loginedUserID(1L)
                        .categoryName("modified_category")
                        .build());

        // then
        verify(categoryRepository, times(1)).save(
                Category.builder()
                        .id(1L)
                        .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                        .categoryName("modified_category")
                        .categoryDesc("test_category_desc")
                        .build()
        );
    }

    @Test
    @DisplayName("patchCategory - 정상동작 (카테고리 설명만 수정)")
    void patchCategory_7() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
        );
        when(
                categoryRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Category.builder()
                                .id(1L)
                                .categoryName("test_category")
                                .categoryDesc("test_category_desc")
                                .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                                .build()
                )
        );

        // when
        blogService.patchCategory(
                PatchCategoryRequest.builder()
                        .blogID(1L)
                        .categoryID(1L)
                        .loginedUserID(1L)
                        .categoryDesc("modified_category_desc")
                        .build());

        // then
        verify(categoryRepository, times(1)).save(
                Category.builder()
                        .id(1L)
                        .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                        .categoryName("test_category")
                        .categoryDesc("modified_category_desc")
                        .build()
        );
    }

    @Test
    @DisplayName("getBlog - 블로그 없음")
    void getBlog_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.getBlog(1L));
    }

    @Test
    @DisplayName("getBlog - 정상동작")
    void getBlog_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .blogName("test_blog")
                                .user(User.builder()
                                        .id(1L)
                                        .nickname("test_nickname")
                                        .profileImageURL("test_profile_image_url")
                                        .build()
                                )
                                .shortIntro("test_short_intro")
                                .introHTML("test_intro_html")
                                .build()
                )
        );

        // when
        GetBlogResponse got = blogService.getBlog(1L);

        GetBlogResponse want = GetBlogResponse.builder()
                .blogID(1L)
                .blogName("test_blog")
                .blogUser(
                        BlogUserDTO.builder()
                                .userID(1L)
                                .nickname("test_nickname")
                                .profileImageURL("test_profile_image_url")
                                .build()
                )
                .shortIntro("test_short_intro")
                .introHTML("test_intro_html")
                .build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("deleteCategory - 블로그 없음")
    void deleteCategory_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.deleteCategory(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteCategory - 카테고리 없음")
    void deleteCategory_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                categoryRepository.findById(1L)
        ).thenThrow(CategoryNotFoundException.class);

        // when + then
        Assertions.assertThrows(CategoryNotFoundException.class, () -> blogService.deleteCategory(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteCategory - 블로그 주인이 아님")
    void deleteCategory_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.deleteCategory(1L, 1L, -1L));
    }

    @Test
    @DisplayName("deleteCategory - 카테고리 내 블로그의 주인과 로그인한 유저가 다름")
    void deleteCategory_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(2L).build())
                                .build()
                )
        );
        when(
                categoryRepository.findById(2L)
        ).thenReturn(
                Optional.of(
                        Category.builder()
                                .id(2L)
                                .blog(Blog.builder().id(4L).user(User.builder().id(-1L).build()).build())
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.deleteCategory(1L, 2L, 2L));
    }

    @Test
    @DisplayName("deleteCategory - 정상동작")
    void deleteCategory_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                categoryRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Category.builder()
                                .id(1L)
                                .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                                .build()
                )
        );

        // when
        blogService.deleteCategory(1L, 1L, 1L);

        // then
        verify(categoryRepository, times(1)).delete(
                Category.builder()
                        .id(1L)
                        .blog(Blog.builder().id(1L).user(User.builder().id(1L).build()).build())
                        .build()
        );
    }


    @Test
    @DisplayName("listTags - 블로그가 없음")
    void listTags_1() {
        // given
        when(
                blogRepository.existsById(1L)
        ).thenReturn(false);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.listTags(1L));
    }

    @Test
    @DisplayName("listTags - 정상동작")
    void listTags_2() {
        // given
        when(
                blogRepository.existsById(1L)
        ).thenReturn(true);

        when(
                tagRepository.findTagsByBlogID(1L)
        ).thenReturn(
                List.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(1L)
                                .tagName("test_tag_1")
                                .build(),
                        Tag.builder()
                                .id(2L)
                                .blogID(1L)
                                .tagName("test_tag_2")
                                .build()
                )
        );

        // when
        ListTagsResponse got = blogService.listTags(1L);

        // then
        ListTagsResponse want = ListTagsResponse.builder()
                .tags(
                        List.of(
                                TagDTO.builder()
                                        .tagID(1L)
                                        .tagName("test_tag_1")
                                        .build(),
                                TagDTO.builder()
                                        .tagID(2L)
                                        .tagName("test_tag_2")
                                        .build()
                        )
                )
                .build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("checkBlogNameExist - 같은 이름을 가진 중복된 블로그가 존재함")
    void checkBlogNameExist_1() {
        // given
        when(
                blogRepository.existsByBlogName("test_blog")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(BlogNameDuplicatedException.class, () -> blogService.checkBlogNameExist(
                CheckBlogNameExistRequest.builder()
                        .blogName("test_blog")
                        .build()));
    }

    @Test
    @DisplayName("checkBlogNameExist - 중복된 블로그가 없음")
    void checkBlogNameExist_2() {
        // given
        when(
                blogRepository.existsByBlogName("test_blog")
        ).thenReturn(false);

        // when + then
        Assertions.assertDoesNotThrow(() -> blogService.checkBlogNameExist(
                CheckBlogNameExistRequest.builder()
                        .blogName("test_blog")
                        .build()));
    }

    @Test
    @DisplayName("createTag - 블로그가 없음")
    void createTag_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.createTag(1L, 1L,
                CreateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("createTag - 블로그 주인이 아님")
    void createTag_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.createTag(1L, -1L,
                CreateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("createTag - 태그 중복")
    void createTag_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.existsByBlogIDAndTagName(1L, "test_tag")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(TagDuplicatedException.class, () -> blogService.createTag(1L, 1L,
                CreateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("createTag - 정상동작")
    void createTag_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.existsByBlogIDAndTagName(1L, "test_tag")
        ).thenReturn(false);

        when(
                tagRepository.save(
                        Tag.builder()
                                .blogID(1L)
                                .tagName("test_tag")
                                .build()
                )
        ).thenReturn(
                Tag.builder()
                        .id(1L)
                        .blogID(1L)
                        .tagName("test_tag")
                        .build()
        );

        // when
        CreateTagResponse got = blogService.createTag(1L, 1L,
                CreateTagRequest.builder()
                        .tagName("test_tag")
                        .build());

        // then
        CreateTagResponse want = CreateTagResponse.builder()
                .tagID(1L)
                .tagName("test_tag")
                .build();

        Assertions.assertEquals(got, want);
    }

    @Test
    @DisplayName("deleteTag - 블로그가 없음")
    void deleteTag_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.deleteTag(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteTag - 블로그의 주인이 아님")
    void deleteTag_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(2L).build())
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.deleteTag(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteTag - 태그가 없음")
    void deleteTag_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenThrow(TagNotFoundException.class);

        // when + then
        Assertions.assertThrows(TagNotFoundException.class, () -> blogService.deleteTag(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteTag - 태그가 블로그와 매칭되지 않음")
    void deleteTag_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(2L)
                                .tagName("test_tag")
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(BlogTagUnmatchedException.class, () -> blogService.deleteTag(1L, 1L, 1L));
    }

    @Test
    @DisplayName("deleteTag - 정상동작")
    void deleteTag_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(1L)
                                .tagName("test_tag")
                                .build()
                )
        );

        // when
        blogService.deleteTag(1L, 1L, 1L);

        // then
        verify(tagRepository, times(1)).delete(
                Tag.builder()
                        .id(1L)
                        .blogID(1L)
                        .tagName("test_tag")
                        .build()
        );
    }

    @Test
    @DisplayName("updateTag - 블로그가 없음")
    void updateTag_1() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenThrow(BlogNotFoundException.class);

        // when + then
        Assertions.assertThrows(BlogNotFoundException.class, () -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("updateTag - 블로그 주인이 아님")
    void updateTag_2() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(2L)
                                .user(User.builder().id(2L).build())
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(NotProperAuthorityException.class, () -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("updateTag - 태그가 없음")
    void updateTag_3() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenThrow(TagNotFoundException.class);

        // when + then
        Assertions.assertThrows(TagNotFoundException.class, () -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("updateTag - 태그가 블로그와 매칭되지 않음")
    void updateTag_4() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(3L)
                                .tagName("test_tag")
                                .build()
                )
        );

        // when + then
        Assertions.assertThrows(BlogTagUnmatchedException.class, () -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }

    @Test
    @DisplayName("updateTag - 태그명이 중복됨")
    void updateTag_5() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(1L)
                                .tagName("test_tag")
                                .build()
                )
        );
        when(
                tagRepository.existsByBlogIDAndTagName(1L, "modifed_tag")
        ).thenReturn(true);

        // when + then
        Assertions.assertThrows(TagDuplicatedException.class, () -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("modifed_tag")
                        .build()));
    }

    @Test
    @DisplayName("updateTag - 기존 태그와 태그명이 같은 경우 (변경사항 없음)")
    void updateTag_6() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder().id(1L).user(User.builder().id(1L).build()).build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder().id(1L).blogID(1L).tagName("test_tag").build()
                )
        );

        // when + then
        Assertions.assertDoesNotThrow(() -> blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("test_tag")
                        .build()));
    }


    @Test
    @DisplayName("updateTag - 정상동작")
    void updateTag_7() {
        // given
        when(
                blogRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Blog.builder()
                                .id(1L)
                                .user(User.builder().id(1L).build())
                                .build()
                )
        );
        when(
                tagRepository.findById(1L)
        ).thenReturn(
                Optional.of(
                        Tag.builder()
                                .id(1L)
                                .blogID(1L)
                                .tagName("test_tag")
                                .build()
                )
        );
        when(
                tagRepository.existsByBlogIDAndTagName(1L, "modified_tag")
        ).thenReturn(false);

        // when
        blogService.updateTag(1L, 1L, 1L,
                UpdateTagRequest.builder()
                        .tagName("modified_tag")
                        .build());

        // then
        verify(tagRepository, times(1)).save(
                Tag.builder()
                        .id(1L)
                        .blogID(1L)
                        .tagName("modified_tag")
                        .build()
        );
    }
}

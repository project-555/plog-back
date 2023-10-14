package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Tag;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.postgres.BlogRepository;
import com.plogcareers.backend.blog.repository.postgres.CategoryRepository;
import com.plogcareers.backend.blog.repository.postgres.CategoryRepositorySupport;
import com.plogcareers.backend.blog.repository.postgres.TagRepository;
import com.plogcareers.backend.ums.domain.dto.CheckBlogNameExistRequest;
import com.plogcareers.backend.ums.exception.BlogNameDuplicatedException;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryRepositorySupport categoryRepositorySupport;
    private final TagRepository tagRepository;

    public void createCategory(Long blogID, Long loginedUserID, @NotNull CreateCategoryRequest createCategoryRequest) throws BlogNotFoundException, CategoryDuplicatedException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);

        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        if (categoryRepository.existsByBlogAndCategoryName(blog, createCategoryRequest.getCategoryName())) {
            throw new CategoryDuplicatedException();
        }

        categoryRepository.save(createCategoryRequest.toEntity(blog));
    }

    public ListCategoriesResponse listCategories(Long blogID) throws BlogNotFoundException {
        if (!blogRepository.existsById(blogID)) {
            throw new BlogNotFoundException();
        }
        List<Category> categories = categoryRepository.findCategoryByBlogIdOrderByCategoryName(blogID);

        return new ListCategoriesResponse(categories.stream().map(Category::toCategoryDto).toList());
    }

    @Transactional
    public void patchCategory(PatchCategoryRequest request) throws BlogNotFoundException, CategoryNotFoundException, NotProperAuthorityException {
        // 블로그 가져온 후 권한 체크
        Blog blog = blogRepository.findById(request.getBlogID()).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(request.getLoginedUserID())) {
            throw new NotProperAuthorityException();
        }

        // 카테고리 가져온 후 권한 체크
        Category category = categoryRepository.findById(request.getCategoryID()).orElseThrow(CategoryNotFoundException::new);
        if (!category.isOwner(request.getLoginedUserID())) {
            throw new CategoryBlogMismatchedException();
        }

        // 카테고리 중복 체크
        if (request.getCategoryName() != null && !request.getCategoryName().isEmpty() && categoryRepositorySupport.existsDuplicatedCategory(request.getBlogID(), request.getCategoryID(), request.getCategoryName())) {
            throw new CategoryDuplicatedException();
        }


        // 파라미터의 값이 없으면 기존 값을 유지한다.
        if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
            category.setCategoryName(request.getCategoryName());
        }
        if (request.getCategoryDesc() != null && !request.getCategoryDesc().isEmpty()) {
            category.setCategoryDesc(request.getCategoryDesc());
        }

        // 카테고리 저장
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long blogID, Long categoryID, Long loginedUserID) throws BlogNotFoundException, CategoryNotFoundException {
        // 블로그 가져온 후 권한 체크
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        // 카테고리 가져온 후 권한 체크
        Category category = categoryRepository.findById(categoryID).orElseThrow(CategoryNotFoundException::new);
        if (!category.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        // 카테고리 삭제
        categoryRepository.delete(category);
    }

    public ListTagsResponse listTags(Long blogID) {
        if (!blogRepository.existsById(blogID)) {
            throw new BlogNotFoundException();
        }
        List<Tag> tags = tagRepository.findTagsByBlogID(blogID);
        return new ListTagsResponse(tags.stream().map(Tag::toTagDTO).toList());
    }

    public CreateTagResponse createTag(Long blogID, Long loginedUserID, CreateTagRequest request) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        if (tagRepository.existsByBlogIDAndTagName(blogID, request.getTagName())) {
            throw new TagDuplicatedException();
        }

        return tagRepository.save(request.toTagEntity(blogID)).toCreateTagResponse();
    }

    public void updateTag(Long blogID, Long tagID, Long loginedUserID, UpdateTagRequest request) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        Tag tag = tagRepository.findById(tagID).orElseThrow(TagNotFoundException::new);
        if (!blog.hasTag(tag)) {
            throw new BlogTagUnmatchedException();
        }
        if (tag.getTagName().equals(request.getTagName())) {
            return;
        }

        if (tagRepository.existsByBlogIDAndTagName(blogID, request.getTagName()))
            throw new TagDuplicatedException();

        tagRepository.save(request.toTagEntity(tag));
    }

    public void deleteTag(Long blogID, Long tagID, Long loginedUserID) {
        // 블로그 가져온 후 권한 체크
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        // 태그 가져온 후 권한 체크
        Tag tag = tagRepository.findById(tagID).orElseThrow(TagNotFoundException::new);
        if (!blog.hasTag(tag)) {
            throw new BlogTagUnmatchedException();
        }

        // 태그 삭제
        tagRepository.delete(tag);
    }

    public void checkBlogNameExist(CheckBlogNameExistRequest request) {
        if (blogRepository.existsByBlogName(request.getBlogName())) {
            throw new BlogNameDuplicatedException();
        }
    }

    public GetBlogResponse getBlog(Long blogID) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        return blog.toGetBlogResponse();
    }

    public void patchBlog(Long loginedUserID, Long blogID, PatchBlogRequest request) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);

        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        blogRepository.save(request.toBlogEntity(blog));
    }
}


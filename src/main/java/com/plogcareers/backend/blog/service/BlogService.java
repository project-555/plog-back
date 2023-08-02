package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.*;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Category;
import com.plogcareers.backend.blog.domain.entity.Tag;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.CategoryRepositortySupport;
import com.plogcareers.backend.blog.repository.CategoryRepository;
import com.plogcareers.backend.blog.repository.TagRepository;
import com.plogcareers.backend.ums.domain.dto.CheckBlogNameExistRequest;
import com.plogcareers.backend.ums.domain.dto.UpdateUserProfileRequest;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.BlogNameDuplicatedException;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
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
    private final CategoryRepositortySupport categoryRepositortySupport;
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
    public void updateCategory(Long blogID, Long loginedUserID, @NotNull UpdateCategoryRequest request) throws BlogNotFoundException, CategoryNotFoundException, NotProperAuthorityException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Category category = categoryRepository.findById(request.getId()).orElseThrow(CategoryNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        if (!category.isOwner(loginedUserID)) {
            throw new CategoryBlogMismatchedException();
        }
        if (categoryRepositortySupport.existsDuplicatedCategory(blogID, request.getId(), request.getCategoryName())) {
            throw new CategoryDuplicatedException();
        }
        categoryRepository.save(request.toCategoryEntity(category, blog));
    }

    @Transactional
    public void deleteCategory(Long blogID, Long categoryID, Long loginedUserID) throws BlogNotFoundException, CategoryNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        Category category = categoryRepository.findById(categoryID).orElseThrow(CategoryNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        if (!category.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }

        categoryRepository.deleteCategoryById(categoryID);
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
        tagRepository.save(request.toTagEntity(tag));
    }

    public void deleteTag(Long blogID, Long tagID, Long loginedUserID) {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        Tag tag = tagRepository.findById(tagID).orElseThrow(TagNotFoundException::new);
        if (!blog.hasTag(tag)) {
            throw new BlogTagUnmatchedException();
        }
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

    public void updateBlogIntro(Long loginedUserID, UpdateBlogIntroRequest request) {
        Blog blog = blogRepository.findById(request.getBlogID()).orElseThrow(BlogNotFoundException::new);
        if (!blog.isOwner(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        blogRepository.save(request.toBlogEntity(blog));
    }
}


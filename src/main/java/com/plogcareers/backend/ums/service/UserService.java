package com.plogcareers.backend.ums.service;

import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.ums.domain.dto.*;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.*;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;


    public GetUserInfoResponse getUser(Long userID) {
        User user = userRepository.findById(userID).orElseThrow(UserNotFoundException::new);
        List<Blog> blogs = blogRepository.findByUser(user);
        if (blogs.isEmpty()) {
            throw new BlogNotFoundException();
        }
        return user.toGetUserInfoResponse(blogs.get(0));
    }

}


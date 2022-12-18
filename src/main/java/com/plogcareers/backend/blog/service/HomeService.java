package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateSubscribeRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Subscribe;
import com.plogcareers.backend.blog.exception.BlogNotFoundException;
import com.plogcareers.backend.blog.exception.NotProperAuthorityException;
import com.plogcareers.backend.blog.exception.SelfSubscribeException;
import com.plogcareers.backend.blog.exception.SubscribeNotFoundException;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.SubscribeRepository;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final SubscribeRepository subscribeRepository;

    public void createSubscribe(Long loginedUserID, @NotNull CreateSubscribeRequest request) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(request.getBlogId()).orElseThrow(BlogNotFoundException::new);
        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        if (!user.getId().equals(loginedUserID)) {
            throw new NotProperAuthorityException();
        }
        if (user.getId().equals(blog.getUser().getId())) {
            throw new SelfSubscribeException();
        }
        subscribeRepository.save(request.toEntity());
    }

    @Transactional
    public void deleteSubscribe(Long blogID, Long loginedUserID) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(blogID).orElseThrow(BlogNotFoundException::new);
        User user = userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new);
        Subscribe subscribe = subscribeRepository.findFirstByUserIdAndBlogId(loginedUserID, blogID).orElseThrow(SubscribeNotFoundException::new);
        subscribeRepository.deleteById(subscribe.getId());
    }
}

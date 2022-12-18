package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateSubscribeRequest;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Subscribe;
import com.plogcareers.backend.blog.exception.*;
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
        if (blog.isSelfSubscribe(user)) {
            throw new SelfSubscribeException();
        }
        if (subscribeRepository.existsByUserIdAndBlogId(user.getId(), blog.getId())) {
            throw new SubscribeDuplicatedException();
        }
        subscribeRepository.save(request.toEntity());
    }

    @Transactional
    public void deleteSubscribe(Long subscribeID, Long loginedUserID) throws BlogNotFoundException {
        User user = userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new);
        Subscribe subscribe = subscribeRepository.findById(subscribeID).orElseThrow(SubscribeNotFoundException::new);
        subscribeRepository.delete(subscribe);
    }
}

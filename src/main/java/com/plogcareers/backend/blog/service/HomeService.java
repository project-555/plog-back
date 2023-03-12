package com.plogcareers.backend.blog.service;

import com.plogcareers.backend.blog.domain.dto.CreateSubscribeRequest;
import com.plogcareers.backend.blog.domain.dto.ListHomePostingsResponse;
import com.plogcareers.backend.blog.domain.dto.ListSubscribesResponse;
import com.plogcareers.backend.blog.domain.entity.Blog;
import com.plogcareers.backend.blog.domain.entity.Subscribe;
import com.plogcareers.backend.blog.domain.entity.VHotPosting;
import com.plogcareers.backend.blog.domain.entity.VPosting;
import com.plogcareers.backend.blog.domain.model.SubscribeDTO;
import com.plogcareers.backend.blog.exception.*;
import com.plogcareers.backend.blog.repository.BlogRepository;
import com.plogcareers.backend.blog.repository.SubscribeRepository;
import com.plogcareers.backend.blog.repository.VHotPostingRepositorySupport;
import com.plogcareers.backend.blog.repository.VPostingRepositorySupport;
import com.plogcareers.backend.ums.domain.entity.User;
import com.plogcareers.backend.ums.exception.NotProperAuthorityException;
import com.plogcareers.backend.ums.exception.UserNotFoundException;
import com.plogcareers.backend.ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final SubscribeRepository subscribeRepository;
    private final VPostingRepositorySupport vPostingRepositorySupport;
    private final VHotPostingRepositorySupport vHotPostingRepositorySupport;

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
        subscribeRepository.save(request.toEntity(blog));
    }

    @Transactional
    public void deleteSubscribe(Long subscribeID, Long loginedUserID) throws BlogNotFoundException {
        User user = userRepository.findById(loginedUserID).orElseThrow(UserNotFoundException::new);
        Subscribe subscribe = subscribeRepository.findById(subscribeID).orElseThrow(SubscribeNotFoundException::new);
        if (!subscribe.isOwner(user)) {
            throw new NotProperAuthorityException();
        }
        subscribeRepository.delete(subscribe);
    }

    public ListSubscribesResponse listSubscribes(Long userID) {
        List<Subscribe> subscribes = subscribeRepository.findByUserId(userID);
        List<SubscribeDTO> subscribeDTOs = subscribes.stream()
                .map(Subscribe::toSubscribeDTO)
                .toList();
        return ListSubscribesResponse.builder().subscribes(subscribeDTOs).build();
    }

    public ListHomePostingsResponse listFollowingPostings(Long loginedUserID, Long lastCursorID, Long pageSize) {
        List<Subscribe> subscribes = subscribeRepository.findByUserId(loginedUserID);
        List<Long> followingIDs = subscribes.stream()
                .map(subscribe -> subscribe.getBlog().getId())
                .toList();
        List<VHotPosting> homePostings = vHotPostingRepositorySupport.listFollowingHomePostings(lastCursorID, followingIDs, pageSize);
        return ListHomePostingsResponse.builder().homePostings(homePostings.stream().map(VHotPosting::toHomePostingDTO).toList()).build();
    }

    public ListHomePostingsResponse listRecentPostings(Long lastCursorID, Long pageSize) {
        List<VPosting> homePostings = vPostingRepositorySupport.listHomePostings(lastCursorID, pageSize);
        return ListHomePostingsResponse.builder().homePostings(
                homePostings.stream()
                        .map(VPosting::toHomePostingDTO)
                        .toList()
        ).build();
    }

}

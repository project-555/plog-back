package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateSubscribeRequest {
    @NotNull
    @ApiParam(value = "유저 ID")
    private Long userId;
    @ApiParam(value = "구독할 블로그 ID")
    private Long blogId;

    public Subscribe toEntity() {
        return Subscribe.builder()
                .userId(userId)
                .blogId(blogId)
                .createDt(LocalDateTime.now())
                .build();
    }
}

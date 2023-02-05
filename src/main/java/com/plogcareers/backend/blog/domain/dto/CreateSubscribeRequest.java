package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.entity.Subscribe;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscribeRequest {
    
    @NotNull
    @ApiModelProperty(value = "구독할 유저 ID")
    private Long userId;

    @ApiModelProperty(value = "구독할 블로그 ID")
    private Long blogId;

    public Subscribe toEntity() {
        return Subscribe.builder()
                .userId(userId)
                .blogId(blogId)
                .createDt(LocalDateTime.now())
                .build();
    }
}

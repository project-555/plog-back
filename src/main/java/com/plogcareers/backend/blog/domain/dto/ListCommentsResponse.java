package com.plogcareers.backend.blog.domain.dto;

import com.plogcareers.backend.blog.domain.model.CommentDTO;
import com.plogcareers.backend.common.domain.dto.SOPagingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ListCommentsResponse {
    List<CommentDTO> comments;

    public SOPagingResponse<List<CommentDTO>> toOPagingResponse(int page, int pageSize, Long tCnt) {
        return new SOPagingResponse<>(this.comments, page, pageSize, tCnt);
    }
}

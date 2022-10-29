package com.plogcareers.backend.common.error;

import com.plogcareers.backend.common.domain.dto.SErrorResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "std-errors")
@Getter
@Setter
public class ErrorMapper {

    Map<String, SErrorResponse> errors = new HashMap<>();

    public SErrorResponse toErrorResponse(ErrorCode errorCode) {
        return errors.get(errorCode.name());
    }
}
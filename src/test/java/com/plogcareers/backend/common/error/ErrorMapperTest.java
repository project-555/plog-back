package com.plogcareers.backend.common.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;



class ErrorMapperTest {
    @Autowired
    private ErrorMapper errorMapper = new ErrorMapper();

    @Test
    void toErrorResponse() {
        System.out.println(errorMapper.toErrorResponse("EMAIL_ALREADY_EXIST").getCode());
    }
}
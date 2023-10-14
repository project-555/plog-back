package com.plogcareers.backend.ums.component;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthorizationCodeGenerator {
    public String generate(int length, AuthorizationCodeCase stringCase) {
        String code = RandomStringUtils.randomAlphanumeric(length);
        if (stringCase == AuthorizationCodeCase.UPPER_CASE) {
            return code.toUpperCase();
        } else if (stringCase == AuthorizationCodeCase.LOWER_CASE) {
            return code.toLowerCase();
        } else {
            return code;
        }
    }
}

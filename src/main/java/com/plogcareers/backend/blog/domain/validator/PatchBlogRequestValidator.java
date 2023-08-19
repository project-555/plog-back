package com.plogcareers.backend.blog.domain.validator;

import com.plogcareers.backend.blog.domain.dto.PatchBlogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PatchBlogRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PatchBlogRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PatchBlogRequest request = (PatchBlogRequest) target;

        if (request.getIntroHTML() == null && request.getIntroMd() != null) {
            errors.rejectValue("introMd", "409", "introHTML과 introMd는 함꼐 수정되어야하는 값입니다.");
        }

        if (request.getIntroHTML() != null && request.getIntroMd() == null) {
            errors.rejectValue("introHTML", "409", "introHTML과 introMd는 함꼐 수정되어야하는 값입니다.");
        }
    }

}

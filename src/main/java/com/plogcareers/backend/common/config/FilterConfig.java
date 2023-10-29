package com.plogcareers.backend.common.config;

import com.plogcareers.backend.common.filter.ControllerLoggingFilter;
import com.plogcareers.backend.common.filter.InjectRequestIDFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // InjectRequestIDFilter 는 ControllerLoggingFilter 보다 우선되어 실행 되어야 하므로 우선 순위를 조정한다.
    @Bean
    public FilterRegistrationBean<InjectRequestIDFilter> loggingFilter() {
        FilterRegistrationBean<InjectRequestIDFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new InjectRequestIDFilter());
        registrationBean.setOrder(1); // 제일 먼저 수행

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ControllerLoggingFilter> securityFilter() {
        FilterRegistrationBean<ControllerLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ControllerLoggingFilter(requestMappingHandlerMapping));
        registrationBean.setOrder(2);  // InjectRequestIDFilter 후에 실행됨

        return registrationBean;
    }
}

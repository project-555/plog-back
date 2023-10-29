package com.plogcareers.backend.common.filter;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InjectRequestIDFilter implements Filter {
    private static final String REQUEST_ID_HEADER = "requestID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 헤더에서 request_id 값 확인
        String requestID = httpRequest.getHeader(REQUEST_ID_HEADER);

        // requestID가 없는 경우 새로 주입한다.
        if (StringUtil.isNullOrEmpty(requestID)) {
            // UUID 생성
            requestID = UUID.randomUUID().toString();

            // 래퍼를 사용하여 요청을 감싸고 request_id 값을 설정
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(httpRequest);
            mutableRequest.putHeader(REQUEST_ID_HEADER, requestID);

            chain.doFilter(mutableRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}

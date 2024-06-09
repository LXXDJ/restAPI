package com.ohgiraffers.comprehensive.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.comprehensive.common.exception.dto.response.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.FAIL_LOGIN;
import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.UNAUTHORIZED;

/* 인증 실패 시 응답 설정 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        /* 유효한 자격 증명(token)을 제공하지 않고 접근하려는 상황 -> 인증 실패 401 */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(UNAUTHORIZED)));
    }
}

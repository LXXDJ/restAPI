package com.ohgiraffers.comprehensive.auth.filter;

import com.ohgiraffers.comprehensive.auth.dto.TokenDto;
import com.ohgiraffers.comprehensive.auth.service.AuthService;
import com.ohgiraffers.comprehensive.auth.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* 인증 확인이 필요한 요청이 왔을 때 확인하는 작업이 작성 된 Filter */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /* 1. 사용자 헤더에서 refresh token 추출 */
        String refreshToken = TokenUtils.getToken(request.getHeader("Refresh-Token"));

        /* 2. Refresh Token이 있다면 ?
        * Access Token 만료 상황이므로 DB에서 Token 일치 여부 확인 후 일치하면 Token 재발급 후 응답
        * */
        if(refreshToken != null && TokenUtils.isValidToken(refreshToken)) {
            TokenDto token = authService.checkRefreshTokenAndReIssueToken(refreshToken);
            response.setHeader("Access-Token", token.getAccessToken());
            response.setHeader("Refresh-Token", token.getRefreshToken());
            return;
        }

        /* 3. Refresh Token이 없다면 ?
        * Access Token 추출하여 유효성 확인 후 Authentication 저장 후 기능 수행
        * */
        String accessToken = TokenUtils.getToken(request.getHeader("Access-Token"));
        if(accessToken != null && TokenUtils.isValidToken(accessToken)) {
            String memberId = TokenUtils.getMemberId(accessToken);
            authService.saveAuthentication(memberId);
        }

        /* access token을 전달한 경우 다음 필터로 진행
        * 그리고 token 없이 요청이 발생한 경우도 다음 필터로 진행
        * */
        filterChain.doFilter(request, response);

    }
}

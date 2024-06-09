package com.ohgiraffers.comprehensive.member.presentation;

import com.ohgiraffers.comprehensive.member.dto.request.MemberSignupRequest;
import com.ohgiraffers.comprehensive.member.dto.response.ProfileResponse;
import com.ohgiraffers.comprehensive.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* 회원 가입 */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignupRequest memberRequest) {

        memberService.signup(memberRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 인증 테스트를 위한 메소드 */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test 응답 완료");
    }

    /* 본인 프로필 조회 */
    @GetMapping("/{memberId}")
    @PreAuthorize("#memberId == authentication.principal.username")
//    @PostAuthorize("returnObject.writer == authentication.principal.username")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String memberId) {

        ProfileResponse profileResponse = memberService.getProfile(memberId);

        return ResponseEntity.ok(profileResponse);
    }

    /* 로그아웃 시 DB 토큰 무효화 */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {

        memberService.updateRefreshToken(userDetails.getUsername(), null);

        return ResponseEntity.ok().build();
    }














}

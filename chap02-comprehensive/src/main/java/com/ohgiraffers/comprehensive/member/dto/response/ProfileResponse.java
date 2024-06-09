package com.ohgiraffers.comprehensive.member.dto.response;

import com.ohgiraffers.comprehensive.member.domain.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileResponse {

    private final String memberId;
    private final String memberName;
    private final String memberEmail;


    public static ProfileResponse from(Member member) {

        return new ProfileResponse(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail()
        );
    }
}

package com.ohgiraffers.comprehensive.member.domain.entity;

import com.ohgiraffers.comprehensive.member.domain.type.MemberRole;
import com.ohgiraffers.comprehensive.member.domain.type.MemberStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberCode;
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberEmail;
    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole = MemberRole.USER;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;
    private String refreshToken;

    private Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public static Member of(String memberId, String memberPassword, String memberName, String memberEmail) {
        return new Member(
                memberId,
                memberPassword,
                memberName,
                memberEmail
        );
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

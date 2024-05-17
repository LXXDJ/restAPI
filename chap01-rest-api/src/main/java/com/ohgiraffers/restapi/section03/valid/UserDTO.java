package com.ohgiraffers.restapi.section03.valid;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private int no;
    @NotNull(message = "아이디 필수입력")    // null값 불허
    @NotBlank(message = "아이디 공백불가")   // 빈값 불허
    private String id;
    @NotNull(message = "비밀번호 필수입력")
    @NotBlank(message = "비밀번호 공백불가")
    @Length(max = 10, message = "비밀번호 최대 길이 10")
    private String pwd;
    @NotNull(message = "이름 필수입력")
    @NotBlank(message = "이름 공백불가")
    @Size(max = 10, message = "이름 최대 길이 10")
    private String name;
    @Past(message = "현재보다 과거 날짜 입력")    // @Future(message = "현재보다 미래 날짜 입력")
    private Date enrollDate;
}

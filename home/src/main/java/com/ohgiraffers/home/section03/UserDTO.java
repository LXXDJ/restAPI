package com.ohgiraffers.home.section03;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private int no;

    @NotNull(message = "아이디 필수입력")
    @NotBlank(message = "아이디 공백불가")
    private String id;

    @NotNull(message = "비밀번호 필수입력")
    @NotBlank(message = "비밀번호 공백불가")
    @Length(max = 10, message = "비밀번호 최대길이 10")
    private String pwd;

    @NotNull(message = "이름 필수입력")
    @NotBlank(message = "이름 공백불가")
    @Size(max = 10, message = "이름 최대길이 10")
    private String name;

    @Past(message = "현재기준 과거날짜 입력")
    private Date enrollDate;

}

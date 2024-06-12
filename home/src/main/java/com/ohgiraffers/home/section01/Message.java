package com.ohgiraffers.home.section01;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Getter
@ToString
public class Message {
    private int httpStatusCode;
    private String message;
}

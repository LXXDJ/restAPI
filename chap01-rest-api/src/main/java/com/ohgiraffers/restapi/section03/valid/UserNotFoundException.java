package com.ohgiraffers.restapi.section03.valid;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String msg) {
        super(msg); // msg를 상위타입으로 전달
    }
}

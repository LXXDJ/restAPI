package com.ohgiraffers.home.section03;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistException(UserNotFoundException e){
        String code = "ERROR_CODE_00000";
        String description = "회원정보 조회실패";
        String detail = e.getMessage();

        return new ResponseEntity<>(new ErrorResponse(code, description, detail), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e){
        String code = "";
        String description = "";
        String detail = "";

        if(e.getBindingResult().hasErrors()){
            detail = e.getBindingResult().getFieldError().getDefaultMessage();
            String bindResultCode = e.getBindingResult().getFieldError().getCode();
            System.out.println(bindResultCode);

            switch(bindResultCode){
                case "NotNull":
                    code = "ERROR_CODE_00001";
                    description = "필수값 누락";
                    break;
                case "NotBlank":
                    code = "ERROR_CODE_00002";
                    description = "필수값 공백처리";
                    break;
                case "Size":
                    code = "ERROR_CODE_00003";
                    description = "알맞지 않은 크기의 값 입력";
                    break;
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(code, description, detail);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

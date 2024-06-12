package com.ohgiraffers.home.section03;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/valid")
public class ValidTestController {
    @GetMapping("/users/{userNo}")
    public ResponseEntity<Void> findUserByNo() throws UserNotFoundException {
        boolean check = true;
        if(check) throw new UserNotFoundException("회원정보 못찾");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registUser(@Valid @RequestBody UserDTO user){
        return ResponseEntity.created(URI.create("/valid/users/1")).build();
    }
}

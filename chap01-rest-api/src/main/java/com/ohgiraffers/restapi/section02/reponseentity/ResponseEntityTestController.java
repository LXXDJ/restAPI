package com.ohgiraffers.restapi.section02.reponseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {
    private List<UserDTO> users;
    
    public ResponseEntityTestController(){
        users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new Date()));
    }
    
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers(){
        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        
        // 응답 데이터 설정
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);
        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);
        
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserbyNo(@PathVariable int userNo){
        // 응답 헤더 설정 : 위에서 복붙
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);

        // 응답 데이터 설정 : 위에서 복붙
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", foundUser);    // foundUser를 put 한다
        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);

        return ResponseEntity.ok().headers(headers).body(responseMessage);
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registUser(@RequestBody UserDTO newUser){   // <Void> : body에 보낼 응답이 없음
        int lastUserNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastUserNo + 1);
        newUser.setEnrollDate(new Date());
        users.add(newUser);

        return ResponseEntity.created(URI.create("/entity/users/" + users.get(users.size() - 1).getNo())).build();
    }

    @PutMapping("/users/{userNo}")
    public ResponseEntity<Void> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo){
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity.created(URI.create("/entity/users/" + userNo)).build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<Void> removeUser(@PathVariable int userNo){
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).toList().get(0);
        users.remove(foundUser);

        return ResponseEntity.noContent().build();
    }
}

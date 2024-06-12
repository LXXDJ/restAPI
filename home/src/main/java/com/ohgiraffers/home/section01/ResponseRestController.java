package com.ohgiraffers.home.section01;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/response")
public class ResponseRestController {
    @GetMapping("/hello")
    public String helloworld(){
        return "hello world";
    }

    @GetMapping("/random")
    public int getRandomNumber(){
        return (int) ((Math.random()*10)+1);
    }

    @GetMapping("/message")
    public Message getMessage(){
        return new Message(200, "메세지 응답");
    }

    @GetMapping("/list")
    public List<String> getList(){
        return List.of(new String[] {"a", "b", "c"});
    }

    @GetMapping("/map")
    public Map<Integer, String> getMap(){
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상응답"));
        messageList.add(new Message(404, "페이지 못찾"));
        messageList.add(new Message(500, "개발자 잘못"));

        return messageList.stream().collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage() throws IOException {
        return getClass().getResourceAsStream("/images/spring.png").readAllBytes();
    }

    @GetMapping("/entity")
    public ResponseEntity<Message> getEntity(){
        return ResponseEntity.ok(new Message(123, "hello world"));
    }
}

package me.silloy.controller;

import me.silloy.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/23
 * Time: 9:44
 * Description: MAIN
 */
@RestController
@RequestMapping
public class MessageController {

    @GetMapping("/sms")
    public Flux<Message> allMessages(){
        return Flux.just(
                Message.builder().body("hello Spring 5").build(),
                Message.builder().body("hello Spring Boot 2").build()
        );
    }
}

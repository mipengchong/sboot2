package com.zj.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/6/25
 * Time: 15:08
 * Description: MAIN
 */
@RestController
public class HelloController {

    @GetMapping("/hello_world")
    public Mono<String> sayHelloWorld() {
        return Mono.just("Hello World");
    }
}

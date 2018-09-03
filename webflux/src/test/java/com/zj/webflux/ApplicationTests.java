package com.zj.webflux;

import com.zj.webflux.controller.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/23
 * Time: 9:46
 * Description: MAIN
 */
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = MessageController.class)
public class ApplicationTests {

    @Autowired
    WebTestClient client;

    @Test
    public void getAllMessagesShouldBeOk() {
        client.get().uri("/sms").exchange().expectStatus().isOk();
    }


    @Test
    public void hello() {
        client.get().uri("/hello").exchange().expectStatus().isOk().expectBody().returnResult().getResponseBody().toString();
    }



}

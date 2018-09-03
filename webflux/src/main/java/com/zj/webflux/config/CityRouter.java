package com.zj.webflux.config;

import com.zj.webflux.handler.CityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/22
 * Time: 20:57
 * Description: MAIN
 */
@Configuration
public class CityRouter {


    @Bean
    public RouterFunction<ServerResponse> routeCity(CityHandler cityHandler) {
        return RouterFunctions
                .route(GET("/hello")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        cityHandler::helloCity)
                .andRoute(GET("/world")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        cityHandler::helloCity);
    }


//    @Bean
//    public RouterFunction<ServerResponse> routes() {
//        return route(GET("/"), (ServerRequest req)-> ok()
//                .body(
//                        BodyInserters.fromObject(
//                                Arrays.asList(
//                                        Message.builder().body("hello Spring 5").build(),
//                                        Message.builder().body("hello Spring Boot 2").build()
//                                )
//                        )
//                )
//        );
//    }

}

package com.zj.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/6 18:02
 * @verion 1.0
 */
@Component
@Order(1)
public class PreFluxFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
//        String userId = queryParams.getFirst("userId");
//        String deliverId = new StringBuilder(userId).append("_").append(System.currentTimeMillis()).append("_req").toString();
        exchange.getAttributes().put("deliverId", "234344");
        return chain.filter(exchange);
    }
}


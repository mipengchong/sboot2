package com.zj.web.service;

import com.zj.web.model.OrderInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/6/24
 * Time: 18:58
 * Description: MAIN
 */
public interface OrderService {

    int saveOrderInfo(OrderInfo oreder);

    Flux<OrderInfo> list();

    Mono<OrderInfo> getById(String id);

    Mono<OrderInfo> createOrUpdate(OrderInfo user);

    Mono<OrderInfo> delete(String id);
}

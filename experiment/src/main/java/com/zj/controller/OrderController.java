package com.zj.controller;

import com.zj.model.OrderInfo;
import com.zj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * OrderInfo: SuShaohua
 * Date: 2018/6/25
 * Time: 17:55
 * Description: MAIN
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
    @ExceptionHandler(Exception.class)
    public void notFound() {
    }

    @GetMapping("")
    public Flux<OrderInfo> list() {
        return this.orderService.list();
    }

    @GetMapping("/{id}")
    public Mono<OrderInfo> getById(@PathVariable("id") final String id) {
        return this.orderService.getById(id);
    }

    @PostMapping("")
    public Mono<OrderInfo> create(@RequestBody final OrderInfo user) {
        return this.orderService.createOrUpdate(user);
    }

    @PutMapping("/{id}")
    public Mono<OrderInfo>  update(@PathVariable("id") final Long id, @RequestBody final OrderInfo user) {
        Objects.requireNonNull(user);
        return this.orderService.createOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public Mono<OrderInfo>  delete(@PathVariable("id") final String id) {
        return this.orderService.delete(id);
    }


}

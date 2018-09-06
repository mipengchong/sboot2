package me.silloy.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/3 18:07
 * @verion 1.0
 */
@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String get() {
        log.info("abc");
        MDC.put("X-B3-TraceId", MDC.get("X-B3-TraceId"));
        return MDC.get("X-B3-TraceId");
    }
}

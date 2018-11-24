package com.zj.web.filter;


import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/6 18:25
 * @verion 1.0
 */
@Component
public class PerformanceInterceptor implements WebFilter {

    private static ThreadLocal<StopWatch> local = new ThreadLocal<>();

    /**
     * 追踪ID
     */
    private final static String TRACE_KEY = "traceId";

    private final Logger switchLogger = LoggerFactory.getLogger("org.perf4j.TimingLogger");


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        StopWatch stopWatch = new Slf4JStopWatch("shell");
        local.set(stopWatch);
        MDC.put(TRACE_KEY, UUID.randomUUID().toString().replace("-", ""));

        chain.filter(exchange);

        StopWatch watch = local.get();
        if (watch != null) {
            watch.stop(generateOperatonIdendifier(exchange.getRequest(), watch.getElapsedTime()));
            local.remove();
        }
        MDC.remove(TRACE_KEY);
        return chain.filter(exchange);
    }


    private String generateOperatonIdendifier(ServerHttpRequest request, long exeTime) {
        StringBuilder sb = new StringBuilder(64);

        sb.append(request.getMethod()).append("|");

        // URI
        if (switchLogger.isTraceEnabled()) { // 如果是trace级别，统计到具体的URI
            sb.append(request.getURI());
            sb.append('|');
//            String clientIp = IpUtil.getIp(request);
//            sb.append(clientIp);
            sb.append('|');
            sb.append(request.getHeaders());
        } else { // 按URI pattern匹配，方便汇总
//            sb.append(request.getQueryParams(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        }

        // 记录慢得url,
        if (exeTime > 100) {
            sb.append("|SLOW");
        }

        return sb.toString();
    }
}

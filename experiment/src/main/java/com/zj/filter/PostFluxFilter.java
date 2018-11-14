package com.zj.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * <p>Description: </p>
 *
 * @author SuShaohua
 * @date 2018/9/6 17:58
 * @verion 1.0
 */
@Component
@Order(2)
public class PostFluxFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    //对响应OK的结果做处理
                if (originalResponse.getStatusCode() == HttpStatus.OK) {
//                    if (body instanceof Flux) {
//                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
//                        return super.writeWith(fluxBody.map(dataBuffer -> decorate(bufferFactory, dataBuffer)));
//                    } else if (body instanceof Mono) {
//                        Mono<? extends DataBuffer> monoBody = (Mono<? extends DataBuffer>) body;
//                        return super.writeWith(monoBody.map(dataBuffer -> decorate(bufferFactory, dataBuffer)));
//                    }
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }


    /**
     * 图片地址过滤
     * @param bufferFactory
     * @param dataBuffer
     * @return
     */
    private DataBuffer decorate(DataBufferFactory bufferFactory, DataBuffer dataBuffer) {
        if (dataBuffer != null && dataBuffer.readableByteCount() > 0) {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            String res = new String(content, Charset.forName("UTF-8"));
            if (res.contains("qiyipic")) {
                res = res.replaceAll("img\\.baidu\\.com", "img.ldzhao.com");
                return bufferFactory.wrap(res.getBytes(Charset.forName("UTF-8")));
            }
        }
        return dataBuffer;
    }
}


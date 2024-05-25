package com.zhaoli.liojbackendgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 实现 Spring Cloud Gateway 的全局过滤器（GlobalFilter）
 *
 * @author 赵立
 */
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {
    /**
     * AntPathMatcher 类用于路径匹配。主要功能是根据 Ant 风格的路径表达式来进行路径匹配，类似于在 Ant 中使用的路径模式匹配规则
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        //判断路径中是否包含 inner ,只允许内部调用
        if (antPathMatcher.match("/**/inner/**", path)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = dataBufferFactory.wrap("无权限".getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(dataBuffer));
        }
        //放行
        //todo 统一权限校验，通过 JWT 获取登录用户的信息
        return chain.filter(exchange);
    }

    /**
     * 表示此过滤器在所有过滤器中的优先级为 0 （最高）
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

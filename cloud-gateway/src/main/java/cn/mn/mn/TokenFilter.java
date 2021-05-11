package cn.mn.mn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * token过滤器
 */
public class TokenFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //TODO 先从cookie中取token,如果cookie中没有,再从header中取
        String tokenKey = "token";
        String tokenValue = null;
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        HttpCookie httpCookie = serverHttpRequest.getCookies().getFirst(tokenKey);
        if (httpCookie != null) {
            tokenValue = httpCookie.getValue();
        }
        if (StringUtil.isEmpty(tokenValue)) {
            tokenValue = serverHttpRequest.getHeaders().getFirst(tokenKey);
        }
        JwtUtil jwtUtil = new JwtUtil();
        String tempPath = serverHttpRequest.getURI().getPath();
        boolean isFilter = false;
        if (tempPath.contains("swagger")
                || tempPath.contains("api-docs")
                || tempPath.contains("/rest/user/login")
                || tempPath.contains("/rest/baseApi")
                || tempPath.contains("/rest/api")
                || tempPath.contains("/rest/common/getVersion")
                || tempPath.contains("/rest/common/getCipherKey")
                || tempPath.contains("/rest/common/encrypt")
                || tempPath.contains("/rest/common/decrypt")
                || tempPath.contains("/rest/user/getOneByToken")){
            isFilter = true;
        }
        if (!isFilter && (StringUtil.isEmpty(tokenValue) || (jwtUtil.parseJWTByUserId(tokenValue) == null
                && jwtUtil.parseJWTByAppId(tokenValue) == null))) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

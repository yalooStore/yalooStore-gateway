package com.yaloostore.gateway.filter;

import com.yalooStore.common_utils.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * jwt를 사용한 토큰 유효성 검사에 사용되는 필터 클래스입니다.
 * */
@Component
@Slf4j
public class CustomAuthorizationFilter extends AbstractGatewayFilterFactory<CustomAuthorizationFilter.Config> {


    @RequiredArgsConstructor
    public static class Config{
        // 필요 설정을 여기에서 진행
        //private final RedisTemplate<String, Object> redisTemplate;
        private final RestTemplate restTemplate;
        private final String authUrl;

    }
    public CustomAuthorizationFilter(){
        super(Config.class);
    }


    /**
     * 해당 조건문에 맞는 요청어오면 jwt이 있는지 유효한지를 확인하는 작업을 진행합니다.
     *
     * @param config 필터에서 사용할 설정
     * @return 토큰 유효성 검사를 진행하는 gatewayFilter
     * */
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");


            //넘어온 Authorization 헤더가 없으면 UNAUTHORIZATION를 날림
            if (Objects.isNull(token)){
                log.info("Authorization header not exist");
                return unAuthorizedHandler(exchange);
            }

            // Authorization: Bearer 로 시작되기 때문에 이 역시 확인 작업을 진행
            String accessToken = prefixRemoveToken(token);
            if (Objects.isNull(accessToken)){
                log.info("해당 토큰이 Bearer로 시작하지 않습니다.");
                return unAuthorizedHandler(exchange);
            }

            boolean isValidToken = checkIsValidToken(accessToken,config);

            if (!isValidToken){
                log.info("is not Valid Token");
                return unAuthorizedHandler(exchange);
            }

            return chain.filter(exchange);
        }));
    }

    private boolean checkIsValidToken(String accessToken, Config config) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        log.info("jwt header!!! === {} ", headers.get("Authorization").toString());
        HttpEntity entity = new HttpEntity(headers);

        URI uri = UriComponentsBuilder.fromUriString(config.authUrl)
                .pathSegment("authorizations", "isValidToken").build().toUri();

        ResponseEntity<ResponseDto<Void>> response = config.restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });

        log.info(String.valueOf(response.getBody().isSuccess()));

        if(!response.getBody().isSuccess()){
            return false;
        }

        return true;
    }

    private String prefixRemoveToken(String token) {

        if (!token.startsWith("Bearer ")){
            return null;
        }

        return token.substring(7);
    }

    private Mono<Void> unAuthorizedHandler(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }



}

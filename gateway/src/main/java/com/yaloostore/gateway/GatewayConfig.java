//package com.yaloostore.gateway;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//
///**
// * 게이트웨이의 라우팅 설정을 위한 자바 설정 파일입니다
// * */
//@Slf4j
//@Configuration
//public class GatewayConfig {
//
//    private String frontUrl;
//    private String shopUrl;
//    private String authUrl;
//
//
//    private List<String> frontUrlPattern = List.of("/members/**", "/", "/error",
//            "/products/**" , "/product/**", "/auth-login", "/static/**",
//            "/assets/**", "/css/**", "/js/**", "/img/**", "/fonts/**");
//
//
//    private List<String> shopUrlPattern = List.of("/api/**");
//    private List<String> authUrlPattern = List.of("/auth/**");
//    private String requestTokenUrlPattern = "/authorizations";
//
//
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder){
//
//        RouteLocatorBuilder.Builder routes= builder.routes();
//
//        for (String patter: frontUrlPattern){
//            routes.route("auth", r -> r.path(patter).uri(frontUrl));
//        }
//
//        routes.route("shop", r -> r.path(shopUrlPattern.get(0)).uri(shopUrl))
//                .route("auth", r -> r.path(authUrlPattern.get(0)).uri(authUrl))
//                .route("token", r -> r.path(requestTokenUrlPattern)
//                        .filters(tokenFilter()).uri(authUrl))
//        ;
//
//        return routes.build();
//    }
//
//
//
//
//
//}

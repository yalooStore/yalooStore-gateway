package com.yaloostore.gateway.config;

import com.yaloostore.gateway.filter.CustomAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "yaloostore")
public class GatewayConfig {
    private String shopUrl;
    private String tokenUrlPattern;
    private String authUrl;


    @Bean
    public RouteLocator routeLocator(CustomAuthorizationFilter authorizationFilter,
                                     RestTemplate restTemplate,
                                     RouteLocatorBuilder builder) {
        return builder.routes()
                .route("token", r-> r.path(tokenUrlPattern)
                        .filters(tokenFilter(authorizationFilter, restTemplate))
                        .uri(shopUrl))
                .build();
    }

    private Function<GatewayFilterSpec, UriSpec> tokenFilter(CustomAuthorizationFilter filter,
                                                             RestTemplate restTemplate) {
        return f -> f.filter(
                filter.apply(
                        new CustomAuthorizationFilter.Config(restTemplate, authUrl)
                )
        );

    }

    public String getTokenUrlPattern() {
        return tokenUrlPattern;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public void setTokenUrlPattern(String tokenUrlPattern) {
        this.tokenUrlPattern = tokenUrlPattern;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }
}
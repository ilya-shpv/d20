package com.ilya.gateway.infra.route;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class SampleRoute {

    private Function<PredicateSpec, Route.AsyncBuilder> addCustomHeader = predicateSpec -> predicateSpec
            .path("/headers")
            .filters(f -> f.addRequestHeader("Hello", "World"))
            .uri("http://httpbin.org:80");

    @Bean
    public RouteLocator sample(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("custom-request-header", addCustomHeader)
                .route("add-query-param", r -> r.path("/get")
                        .filters(f -> f.addRequestParameter("hello", "world"))
                        .uri("http://httpbin.org:80"))
                .route("response-headers", (r) -> r.path("/response-headers")
                        .filters(f -> f.addResponseHeader("hello","world"))
                        .uri("http://httpbin.org:80"))
                .route("combine-and-change", (r) -> r.path("/anything").and().header("access-key","AAA")
                        .filters(f -> f.addResponseHeader("access-key","BBB"))
                        .uri("http://httpbin.org:80"))
                .build();
    }

}

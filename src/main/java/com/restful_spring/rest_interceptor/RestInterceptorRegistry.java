package com.restful_spring.rest_interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class RestInterceptorRegistry {

    private final InterceptorRegistry registry;
    private final List<RestInterceptorRegistration> registrations = new ArrayList<>();

    public RestInterceptorRegistry(InterceptorRegistry registry) {
        this.registry = registry;
    }

    public RestInterceptorRegistration addInterceptor(RestInterceptor restInterceptor) {
        RestInterceptorRegistration registration = new RestInterceptorRegistration(restInterceptor);
        registrations.add(registration);
        return registration;
    }

    public void build() {
        this.registrations.forEach(registration -> {
            RestInterceptor restInterceptor = registration.getRestInterceptor();

            registry.addInterceptor(restInterceptor)
                    .addPathPatterns(restInterceptor.restfulPatterns.stream()
                            .map(RestfulPattern::getPath)
                            .collect(Collectors.toList()))
                    .order(registration.getOrder());
        });
    }
}

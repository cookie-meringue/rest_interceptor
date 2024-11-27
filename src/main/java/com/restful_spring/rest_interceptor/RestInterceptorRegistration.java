package com.restful_spring.rest_interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestInterceptorRegistration {

    private final RestInterceptor restInterceptor;
    private int order = 0;

    public RestInterceptorRegistration(RestInterceptor restInterceptor) {
        this.restInterceptor = restInterceptor;
    }

    public RestInterceptorRegistration addRestfulPatterns(RestfulPattern... restfulPatterns) {
        return addRestfulPatterns(List.of(restfulPatterns));
    }

    public RestInterceptorRegistration addRestfulPatterns(Collection<RestfulPattern> restfulPatterns) {
        restInterceptor.restfulPatterns = new ArrayList<>(restfulPatterns);
        return this;
    }

    public RestInterceptorRegistration order(int order) {
        this.order = order;
        return this;
    }

    protected RestInterceptor getRestInterceptor() {
        return restInterceptor;
    }

    protected int getOrder() {
        return order;
    }
}

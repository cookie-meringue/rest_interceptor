package com.restful_spring.rest_interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Assists with the creation of a {@link RestInterceptor}.
 *
 * @author Dh3356
 * @since 0.1
 */
public class RestInterceptorRegistration {

    private final RestInterceptor restInterceptor;
    private int order = 0;

    /**
     * Creates a new instance of {@link RestInterceptorRegistration}.
     */
    public RestInterceptorRegistration(RestInterceptor restInterceptor) {
        this.restInterceptor = restInterceptor;
    }

    /**
     * Add RestfulPatterns the interceptor should be included in.
     */
    public RestInterceptorRegistration addRestfulPatterns(RestfulPattern... restfulPatterns) {
        return addRestfulPatterns(List.of(restfulPatterns));
    }

    /**
     * List-based variant of {@link #addRestfulPatterns(RestfulPattern...)}.
     */
    public RestInterceptorRegistration addRestfulPatterns(Collection<RestfulPattern> restfulPatterns) {
        restInterceptor.restfulPatterns = new ArrayList<>(restfulPatterns);
        return this;
    }

    /**
     * Specify an order position to be used. Default is 0.
     */
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

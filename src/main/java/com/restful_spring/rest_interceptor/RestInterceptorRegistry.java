package com.restful_spring.rest_interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Helps with configuring a list of RestInterceptors. Adaptor between InterceptorRegistry and RestInterceptor.
 *
 * @author Dh3356
 * @since 0.1
 */
public class RestInterceptorRegistry {

    private final InterceptorRegistry registry;

    public RestInterceptorRegistry(InterceptorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Adds the provided {@link RestInterceptor}.
     *
     * @param restInterceptor the restInterceptor to add
     * @return an {@link RestInterceptorRegistration} that allows you optionally configure the registered
     * restInterceptor further for example adding RestfulPatterns it should apply to.
     */
    public RestInterceptorRegistration addInterceptor(RestInterceptor restInterceptor) {
        return new RestInterceptorRegistration(restInterceptor, registry);
    }
}

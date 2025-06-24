package com.restful_spring.rest_interceptor;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Assists with the creation of a {@link RestInterceptor}.
 *
 * @author cookie-meringue
 * @since 0.1
 */
public final class RestInterceptorRegistration {

    private final RestInterceptor restInterceptor;
    private final InterceptorRegistration registration;

    /**
     * Creates a new instance of {@link RestInterceptorRegistration}.
     */
    RestInterceptorRegistration(RestInterceptor restInterceptor, InterceptorRegistry registry) {
        this.restInterceptor = restInterceptor;
        this.registration = registry.addInterceptor(restInterceptor);
    }

    /**
     * Add RestPatterns the interceptor should be included in.
     */
    public RestInterceptorRegistration addRestPatterns(RestPattern... restPatterns) {
        return addRestPatterns(Arrays.asList(restPatterns));
    }

    /**
     * Collection-based variant of {@link #addRestPatterns(RestPattern...)}.
     */
    public RestInterceptorRegistration addRestPatterns(Collection<RestPattern> restPatterns) {
        return addRestPatterns(RestPatterns.from(restPatterns));
    }

    /**
     * Adds the given RestPatterns to the RestInterceptor and updates path patterns.
     * <p>
     * This method registers the provided RestPatterns with the RestInterceptor and ensures that the corresponding paths
     * are added to the interceptor registration.
     *
     * @param restPatterns the RestPatterns to be registered
     * @return this RestInterceptorRegistration instance for method chaining
     * @since 1.0.2
     */
    RestInterceptorRegistration addRestPatterns(RestPatterns restPatterns) {
        restInterceptor.addRestPatterns(restPatterns);
        registration.addPathPatterns(restPatterns.getPaths());
        return this;
    }

    /**
     * Add RestPatterns the interceptor should be excluded from.
     */
    public RestInterceptorRegistration excludeRestPatterns(RestPattern... restPatterns) {
        return excludeRestPatterns(Arrays.asList(restPatterns));
    }

    /**
     * Collection-based variant of {@link #excludeRestPatterns(RestPattern...)}.
     */
    public RestInterceptorRegistration excludeRestPatterns(Collection<RestPattern> restPatterns) {
        return excludeRestPatterns(RestPatterns.from(restPatterns));
    }

    /**
     * Adds the given RestPatterns to the RestInterceptor and updates exclude path patterns.
     * <p>
     * This method registers the provided RestPatterns with the RestInterceptor and ensures that the corresponding paths
     * are added to the exclude path patterns of the interceptor registration.
     *
     * @param excludePatterns the RestPatterns to be registered
     * @return this RestInterceptorRegistration instance for method chaining
     * @since 1.0.2
     */
    RestInterceptorRegistration excludeRestPatterns(RestPatterns excludePatterns) {
        restInterceptor.addExcludePatterns(excludePatterns);
        registration.excludePathPatterns(excludePatterns.getPaths());
        return this;
    }

    /**
     * Specify an order position to be used. Default is 0.
     */
    public RestInterceptorRegistration order(int order) {
        registration.order(order);
        return this;
    }
}

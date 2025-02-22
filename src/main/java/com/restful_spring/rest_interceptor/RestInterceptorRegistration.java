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
public class RestInterceptorRegistration {

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
     * Add RestfulPatterns the interceptor should be included in.
     */
    public RestInterceptorRegistration addRestfulPatterns(RestfulPattern... restfulPatterns) {
        return addRestfulPatterns(Arrays.asList(restfulPatterns));
    }

    /**
     * Collection-based variant of {@link #addRestfulPatterns(RestfulPattern...)}.
     */
    public RestInterceptorRegistration addRestfulPatterns(Collection<RestfulPattern> restfulPatterns) {
        return addRestfulPatterns(RestfulPatterns.from(restfulPatterns));
    }

    /**
     * Adds the given RestfulPatterns to the RestInterceptor and updates path patterns.
     * <p>
     * This method registers the provided RestfulPatterns with the RestInterceptor and ensures that the corresponding
     * paths are added to the interceptor registration.
     *
     * @param restfulPatterns the RestfulPatterns to be registered
     * @return this RestInterceptorRegistration instance for method chaining
     * @since 1.0.2
     */
    public RestInterceptorRegistration addRestfulPatterns(RestfulPatterns restfulPatterns) {
        restInterceptor.addRestfulPatterns(restfulPatterns);
        registration.addPathPatterns(restfulPatterns.getPaths());
        return this;
    }

    /**
     * Add RestfulPatterns the interceptor should be excluded from.
     */
    public RestInterceptorRegistration excludeRestfulPatterns(RestfulPattern... restfulPatterns) {
        return excludeRestfulPatterns(Arrays.asList(restfulPatterns));
    }

    /**
     * Collection-based variant of {@link #excludeRestfulPatterns(RestfulPattern...)}.
     */
    public RestInterceptorRegistration excludeRestfulPatterns(Collection<RestfulPattern> restfulPatterns) {
        return excludeRestfulPatterns(RestfulPatterns.from(restfulPatterns));
    }

    /**
     * Adds the given RestfulPatterns to the RestInterceptor and updates exclude path patterns.
     * <p>
     * This method registers the provided RestfulPatterns with the RestInterceptor and ensures that the corresponding
     * paths are added to the exclude path patterns of the interceptor registration.
     *
     * @param excludePatterns the RestfulPatterns to be registered
     * @return this RestInterceptorRegistration instance for method chaining
     * @since 1.0.2
     */
    public RestInterceptorRegistration excludeRestfulPatterns(RestfulPatterns excludePatterns) {
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

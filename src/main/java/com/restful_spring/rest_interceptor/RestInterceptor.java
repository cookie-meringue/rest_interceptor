package com.restful_spring.rest_interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for RESTful API.
 * <p> This class is an abstract class that implements {@link HandlerInterceptor}.
 * <p> Request will always be passed if it is a pre-flight request or it is not matched with any of the
 * restfulPatterns.
 *
 * @author cookie-meringue
 * @since 0.1
 */
public abstract class RestInterceptor implements HandlerInterceptor {

    RestPatterns restPatterns = RestPatterns.empty();
    RestPatterns excludePatterns = RestPatterns.empty();

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreFlightRequest(request) || shouldSkip(request)) {
            return true;
        }
        return doInternal(request, response, handler);
    }

    /**
     * Check if the request is a pre-flight request.
     */
    private boolean isPreFlightRequest(final HttpServletRequest request) {
        return CorsUtils.isPreFlightRequest(request);
    }

    /**
     * Check if the request should be skipped.
     * <p> If request path is not matched with any of the restfulPatterns, it should be skipped.
     * <p> If request path is matched with any of the excludePatterns, it should be skipped.
     */
    private boolean shouldSkip(final HttpServletRequest request) {
        return excludePatterns.anyMatches(request) || restPatterns.noneMatches(request);
    }

    /**
     * Core logic of {@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}
     * <p> This method should be implemented by subclasses. Default implementation returns true.
     */
    protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    /**
     * Adds all RestPatterns from the given RestPatterns instance.
     * <p>
     * This method merges the provided RestPatterns into the existing collection.
     *
     * @param restPatterns the RestPatterns to be added
     * @since 1.0.2
     */
    void addRestPatterns(final RestPatterns restPatterns) {
        this.restPatterns.addAll(restPatterns);
    }

    /**
     * Adds all RestPatterns from the given RestPatterns instance.
     * <p>
     * This method merges the provided RestPatterns into the existing collection.
     *
     * @param excludePatterns the RestPatterns to be added
     * @since 1.0.2
     */
    void addExcludePatterns(final RestPatterns excludePatterns) {
        this.excludePatterns.addAll(excludePatterns);
    }
}

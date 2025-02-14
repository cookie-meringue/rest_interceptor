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
 * @author Dh3356
 * @since 0.1
 */
public abstract class RestInterceptor implements HandlerInterceptor {

    protected RestfulPatterns restfulPatterns = RestfulPatterns.empty();

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
     */
    private boolean shouldSkip(final HttpServletRequest request) {
        return restfulPatterns.noneMatches(request);
    }

    /**
     * Core logic of {@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}
     * <p> This method should be implemented by subclasses. Default implementation returns true.
     */
    protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    /**
     * Adds all RestfulPatterns from the given RestfulPatterns instance.
     * <p>
     * This method merges the provided RestfulPatterns into the existing collection.
     *
     * @param restfulPatterns the RestfulPatterns to be added
     * @since Upcoming
     */
    void addRestfulPatterns(final RestfulPatterns restfulPatterns) {
        this.restfulPatterns.addAll(restfulPatterns);
    }
}

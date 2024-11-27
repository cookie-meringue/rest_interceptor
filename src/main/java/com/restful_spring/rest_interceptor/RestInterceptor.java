package com.restful_spring.rest_interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class RestInterceptor implements HandlerInterceptor {

    protected List<RestfulPattern> restfulPatterns = List.of();

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreFlightRequest(request) || shouldSkip(request)) {
            return true;
        }
        return doInternal(request, response, handler);
    }

    private boolean isPreFlightRequest(final HttpServletRequest request) {
        return CorsUtils.isPreFlightRequest(request);
    }

    private boolean shouldSkip(final HttpServletRequest request) {
        return this.restfulPatterns.stream()
                .noneMatch(pattern -> pattern.matches(request));
    }

    protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }
}

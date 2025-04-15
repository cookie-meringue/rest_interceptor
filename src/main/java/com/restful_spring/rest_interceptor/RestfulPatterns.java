package com.restful_spring.rest_interceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * First-class collection of RestfulPattern.
 * <p>
 * Encapsulates a Collection of RestfulPattern and provides apis.
 *
 * @author cookie-meringue
 * @since 1.0.2
 */
final class RestfulPatterns {

    private final List<RestfulPattern> values;

    private RestfulPatterns(List<RestfulPattern> values) {
        this.values = values;
    }

    /**
     * Create an empty RestfulPatterns.
     * <p>
     * Static factory method for creating a RestfulPatterns.
     *
     * @return an empty RestfulPatterns
     */
    static RestfulPatterns empty() {
        return new RestfulPatterns(new ArrayList<>());
    }

    /**
     * Create a RestfulPatterns from a Collection of RestfulPattern.
     * <p>
     * Static factory method for creating a RestfulPatterns.
     *
     * @param values a Collection of RestfulPattern
     * @return a RestfulPatterns
     */
    static RestfulPatterns from(final Collection<RestfulPattern> values) {
        return new RestfulPatterns(new ArrayList<>(values));
    }

    /**
     * Determines whether the given request does not match any RestfulPatterns.
     *
     * @param request the HttpServletRequest to be checked against the patterns
     * @return {@code true} if no patterns match the request, otherwise {@code false}
     */
    boolean noneMatches(final HttpServletRequest request) {
        return values.stream()
            .noneMatch(pattern -> pattern.matches(request));
    }

    /**
     * Determines whether the given request matches any RestfulPatterns.
     *
     * @param request the HttpServletRequest to be checked against the patterns
     * @return {@code true} if any patterns match the request, otherwise {@code false}
     * @since 1.0.2
     */
    boolean anyMatches(final HttpServletRequest request) {
        return values.stream()
            .anyMatch(pattern -> pattern.matches(request));
    }

    /**
     * Add all RestfulPatterns from another RestfulPatterns.
     *
     * @param restfulPatterns another RestfulPatterns
     */
    void addAll(final RestfulPatterns restfulPatterns) {
        values.addAll(List.copyOf(restfulPatterns.values));
    }

    /**
     * Get the paths of the RestfulPatterns.
     *
     * @return a List of paths
     */
    List<String> getPaths() {
        return values.stream()
            .map(RestfulPattern::getPath)
            .toList();
    }
}

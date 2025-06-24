package com.restful_spring.rest_interceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * First-class collection of RestPattern.
 * <p>
 * Encapsulates a Collection of RestPattern and provides apis.
 *
 * @author cookie-meringue
 * @since 1.0.2
 */
final class RestPatterns {

    private final List<RestPattern> values;

    private RestPatterns(List<RestPattern> values) {
        this.values = values;
    }

    /**
     * Create an empty RestPatterns.
     * <p>
     * Static factory method for creating a RestPatterns.
     *
     * @return an empty RestPatterns
     */
    static RestPatterns empty() {
        return new RestPatterns(new ArrayList<>());
    }

    /**
     * Create a RestPatterns from a Collection of RestPattern.
     * <p>
     * Static factory method for creating a RestPatterns.
     *
     * @param values a Collection of RestPattern
     * @return a RestPatterns
     */
    static RestPatterns from(final Collection<RestPattern> values) {
        return new RestPatterns(new ArrayList<>(values));
    }

    /**
     * Determines whether the given request does not match any RestPatterns.
     *
     * @param request the HttpServletRequest to be checked against the patterns
     * @return {@code true} if no patterns match the request, otherwise {@code false}
     */
    boolean noneMatches(final HttpServletRequest request) {
        return values.stream()
                .noneMatch(pattern -> pattern.matches(request));
    }

    /**
     * Determines whether the given request matches any RestPatterns.
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
     * Add all RestPatterns from another RestPatterns.
     *
     * @param restPatterns another RestPatterns
     */
    void addAll(final RestPatterns restPatterns) {
        values.addAll(List.copyOf(restPatterns.values));
    }

    /**
     * Get the paths of the RestPatterns.
     *
     * @return a List of paths
     */
    List<String> getPaths() {
        return values.stream()
                .map(RestPattern::getPath)
                .toList();
    }
}

package com.restful_spring.rest_interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RestInterceptorTest {

    private static final String FOO = "/foo";
    private static final String BAR = "/bar";
    private static final RestfulPattern REGISTRATION_PATTERN = RestfulPattern.of(FOO, GET);
    private RestInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new RestInterceptor() {
            @Override
            protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
                return true;
            }
        };

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preFlightRequest() {
        // Given
        request.setMethod(HttpMethod.OPTIONS.name());

        // When
        boolean result = interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void pathMatchingWithRestfulPattern() {
        // Given
        interceptor.restfulPatterns = RestfulPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(FOO);
        request.setMethod(GET.name());
        boolean result = interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void skipWithNonMatchingPath() {
        // Given
        interceptor.restfulPatterns = RestfulPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(BAR);
        request.setMethod(GET.name());
        boolean result = interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldSkipWhenNoPatterns() {
        // Given
        interceptor.restfulPatterns = RestfulPatterns.empty();

        // When
        request.setRequestURI(FOO);
        boolean result = interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void doInternalCalledForMatchingRequest() {
        // Given
        interceptor.restfulPatterns = RestfulPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(FOO);
        request.setMethod(GET.name());

        // Then
        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isTrue();
    }

    @Test
    void doInternalNotCalledForNonMatchingRequest() {
        // Given
        interceptor.restfulPatterns = RestfulPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(BAR);
        request.setMethod(GET.name());

        // Then
        boolean result = interceptor.preHandle(request, response, new Object());
        assertThat(result).isTrue();
    }
}

package com.restful_spring.rest_interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RestInterceptorTest {

    private static final String FOO = "/foo";
    private static final String BAR = "/bar";
    private static final RestPattern REGISTRATION_PATTERN = RestPattern.of(FOO, GET);
    private static final RestPattern EXCLUSION_PATTERN = RestPattern.of(BAR, POST);
    private final AtomicBoolean doInternalCalled = new AtomicBoolean(false);
    private RestInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        doInternalCalled.set(false);
        interceptor = new RestInterceptor() {
            @Override
            protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
                doInternalCalled.set(true);
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
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isFalse();
    }

    @Test
    void pathMatchingWithRestPattern() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(FOO);
        request.setMethod(GET.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isTrue();
    }

    @Test
    void skipWithNonMatchingPath() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(BAR);
        request.setMethod(GET.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isFalse();
    }

    @Test
    void shouldSkipWhenNoPatterns() {
        // Given
        interceptor.restPatterns = RestPatterns.empty();

        // When
        request.setRequestURI(FOO);
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isFalse();
    }

    @Test
    void doInternalCalledForMatchingRequest() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(FOO);
        request.setMethod(GET.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isTrue();
    }

    @Test
    void doInternalNotCalledForNonMatchingRequest() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));

        // When
        request.setRequestURI(BAR);
        request.setMethod(GET.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isFalse();
    }

    @Test
    void doInternalNotCalledForExcludingRequest() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));
        interceptor.excludePatterns = RestPatterns.from(List.of(EXCLUSION_PATTERN));

        // When
        request.setRequestURI(BAR);
        request.setMethod(POST.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isFalse();
    }

    @Test
    void doInternalCalledForNonExcludingRequest() {
        // Given
        interceptor.restPatterns = RestPatterns.from(List.of(REGISTRATION_PATTERN));
        interceptor.excludePatterns = RestPatterns.from(List.of(EXCLUSION_PATTERN));

        // When
        request.setRequestURI(FOO);
        request.setMethod(GET.name());
        interceptor.preHandle(request, response, new Object());

        // Then
        assertThat(doInternalCalled.get()).isTrue();
    }
}

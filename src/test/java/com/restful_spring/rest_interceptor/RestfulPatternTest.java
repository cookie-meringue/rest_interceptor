package com.restful_spring.rest_interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.TRACE;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Test class for {@link RestfulPattern}.
 *
 * @author cookie-meringue
 * @since 0.2
 */
class RestfulPatternTest {

    private static final String FOO = "/foo";
    private static final String BAR = "/bar";
    private static final String ALL = "/**";

    @Test
    void createFromPath() {
        // Given
        RestfulPattern pattern = RestfulPattern.fromPath(FOO);

        // When
        List<MockHttpServletRequest> requests = Arrays.stream(HttpMethod.values())
                .map(method -> new MockHttpServletRequest(method.name(), FOO))
                .toList();

        // Then
        requests.forEach(request -> assertTrue(pattern.matches(request)));
    }

    @Test
    void pathMatchingWithSingleHttpMethod() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO, GET);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), FOO);

        // Then
        assertTrue(pattern.matches(request));
    }

    @Test
    void pathMatchingWithMultipleHttpMethods() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO, Set.of(GET, POST));

        // When
        MockHttpServletRequest getRequest = new MockHttpServletRequest(GET.name(), FOO);
        MockHttpServletRequest postRequest = new MockHttpServletRequest(POST.name(), FOO);

        // Then
        assertTrue(pattern.matches(getRequest));
        assertTrue(pattern.matches(postRequest));
    }

    @Test
    void pathMatchingWithWildcard() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO + ALL, Set.of(GET));

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), FOO + BAR);

        // Then
        assertTrue(pattern.matches(request));
    }

    @Test
    void pathNotMatching() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO, GET);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), BAR);

        // Then
        assertThat(pattern.matches(request)).isFalse();
    }

    @Test
    void methodNotMatching() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO, GET);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(POST.name(), FOO);

        // Then
        assertThat(pattern.matches(request)).isFalse();
    }

    @Test
    void pathNotMatchingWithWildcard() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO + ALL, Set.of(GET));

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), BAR + FOO);

        // Then
        assertThat(pattern.matches(request)).isFalse();
    }

    @Test
    void pathVariableMatching() {
        // Given
        RestfulPattern pattern = RestfulPattern.of(FOO + "/{id}", GET);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), FOO + "/123");

        // Then
        assertTrue(pattern.matches(request));
    }

    @Test
    void emptyMethodsSet() {
        // Given
        RestfulPattern pattern = RestfulPattern.builder().path(FOO).build();

        // When
        MockHttpServletRequest request = new MockHttpServletRequest(GET.name(), FOO);

        // Then
        assertTrue(pattern.matches(request));
    }

    @Test
    void builderPattern() {
        // Given
        RestfulPattern pattern = RestfulPattern.builder()
                .path(FOO)
                .get()
                .post()
                .build();

        // When
        MockHttpServletRequest getRequest = new MockHttpServletRequest(GET.name(), FOO);
        MockHttpServletRequest postRequest = new MockHttpServletRequest(POST.name(), FOO);

        // Then
        assertTrue(pattern.matches(getRequest));
        assertTrue(pattern.matches(postRequest));
    }

    @Test
    void allMethods() {
        // Given
        RestfulPattern pattern = RestfulPattern.builder().path(FOO).all().build();

        // When
        MockHttpServletRequest getRequest = new MockHttpServletRequest(GET.name(), FOO);
        MockHttpServletRequest postRequest = new MockHttpServletRequest(POST.name(), FOO);

        // Then
        assertTrue(pattern.matches(getRequest));
        assertTrue(pattern.matches(postRequest));
        assertTrue(pattern.matches(new MockHttpServletRequest(PUT.name(), FOO)));
        assertTrue(pattern.matches(new MockHttpServletRequest(DELETE.name(), FOO)));
        assertTrue(pattern.matches(new MockHttpServletRequest(PATCH.name(), FOO)));
        assertTrue(pattern.matches(new MockHttpServletRequest(TRACE.name(), FOO)));
        assertTrue(pattern.matches(new MockHttpServletRequest(OPTIONS.name(), FOO)));
        assertTrue(pattern.matches(new MockHttpServletRequest(HEAD.name(), FOO)));
    }
}


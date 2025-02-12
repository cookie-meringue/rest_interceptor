package com.restful_spring.rest_interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

class RestfulPatternsTest {

    private static List<RestfulPattern> parseValues(RestfulPatterns patterns) {
        try {
            Field valuesField = RestfulPatterns.class.getDeclaredField("values");
            valuesField.setAccessible(true);
            return (List<RestfulPattern>) valuesField.get(patterns);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void emptyCreation() {

        // Given
        RestfulPatterns patterns = RestfulPatterns.empty();

        // When
        List<RestfulPattern> values = parseValues(patterns);
        int actual = values.size();

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void noneMatches() {

        // Given
        RestfulPattern pattern1 = RestfulPattern.of("/foo", HttpMethod.GET);
        RestfulPattern pattern2 = RestfulPattern.of("/bar", HttpMethod.POST);
        RestfulPatterns patterns = RestfulPatterns.from(List.of(pattern1, pattern2));

        // When
        MockHttpServletRequest request1 = new MockHttpServletRequest(HttpMethod.GET.name(), "/bar");
        MockHttpServletRequest request2 = new MockHttpServletRequest(HttpMethod.GET.name(), "/foo");
        MockHttpServletRequest request3 = new MockHttpServletRequest(HttpMethod.PATCH.name(), "/baz");

        // Then
        assertThat(patterns.noneMatches(request1)).isTrue();
        assertThat(patterns.noneMatches(request2)).isFalse();
        assertThat(patterns.noneMatches(request3)).isTrue();
    }

    @Test
    void addAll() {

        // Given
        RestfulPattern pattern1 = RestfulPattern.of("/foo", HttpMethod.GET);
        RestfulPattern pattern2 = RestfulPattern.of("/bar", HttpMethod.POST);
        RestfulPatterns patterns = RestfulPatterns.empty();

        // When
        patterns.addAll(RestfulPatterns.from(List.of(pattern1, pattern2)));
        int actual = parseValues(patterns).size();

        // Then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    void getPaths() {

        // Given
        RestfulPattern pattern1 = RestfulPattern.of("/foo", HttpMethod.GET);
        RestfulPattern pattern2 = RestfulPattern.of("/bar", HttpMethod.POST);
        RestfulPatterns patterns = RestfulPatterns.from(List.of(pattern1, pattern2));

        // When
        List<String> actual = patterns.getPaths();

        // Then
        assertThat(actual).containsExactlyInAnyOrder("/foo", "/bar");
    }
}
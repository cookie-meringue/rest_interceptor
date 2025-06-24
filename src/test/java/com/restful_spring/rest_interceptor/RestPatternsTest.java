package com.restful_spring.rest_interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

class RestPatternsTest {

    private static List<RestPattern> parseValues(RestPatterns patterns) {
        try {
            Field valuesField = RestPatterns.class.getDeclaredField("values");
            valuesField.setAccessible(true);
            return (List<RestPattern>) valuesField.get(patterns);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void emptyCreation() {

        // Given
        RestPatterns patterns = RestPatterns.empty();

        // When
        List<RestPattern> values = parseValues(patterns);
        int actual = values.size();

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void noneMatches() {

        // Given
        RestPattern pattern1 = RestPattern.of("/foo", HttpMethod.GET);
        RestPattern pattern2 = RestPattern.of("/bar", HttpMethod.POST);
        RestPatterns patterns = RestPatterns.from(List.of(pattern1, pattern2));

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
        RestPattern pattern1 = RestPattern.of("/foo", HttpMethod.GET);
        RestPattern pattern2 = RestPattern.of("/bar", HttpMethod.POST);
        RestPatterns patterns = RestPatterns.empty();

        // When
        patterns.addAll(RestPatterns.from(List.of(pattern1, pattern2)));
        int actual = parseValues(patterns).size();

        // Then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    void getPaths() {

        // Given
        RestPattern pattern1 = RestPattern.of("/foo", HttpMethod.GET);
        RestPattern pattern2 = RestPattern.of("/bar", HttpMethod.POST);
        RestPatterns patterns = RestPatterns.from(List.of(pattern1, pattern2));

        // When
        List<String> actual = patterns.getPaths();

        // Then
        assertThat(actual).containsExactlyInAnyOrder("/foo", "/bar");
    }
}

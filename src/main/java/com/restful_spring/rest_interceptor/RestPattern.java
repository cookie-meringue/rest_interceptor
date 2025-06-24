package com.restful_spring.rest_interceptor;

import static org.springframework.http.HttpMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

/**
 * Pattern for matching restful requests.
 * <p> This class is a part of the restful-interceptor module.
 * <p> This class is used to match the request URI and HTTP method.
 * <p> This class is used by {@link RestInterceptor}.
 *
 * @author cookie-meringue
 * @since 0.1
 */
public final class RestPattern {

    private final String path;
    private final Set<HttpMethod> methods;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private RestPattern(final String path, final Set<HttpMethod> methods) {
        this.path = path;
        this.methods = methods;
    }

    /**
     * Create a new instance of {@link RestPattern} with the given path.
     * <p> Default HTTP methods are GET, POST, PUT, DELETE, PATCH, TRACE, OPTIONS, HEAD.
     *
     * @author cookie-meringue
     * @since 1.0
     */
    public static RestPattern fromPath(final String path) {
        return new RestPattern(path, Set.of(HttpMethod.values()));
    }

    /**
     * Create a new instance of {@link RestPattern} with the given path and HTTP methods.
     *
     * @author cookie-meringue
     * @since 1.0
     */
    public static RestPattern of(final String path, final HttpMethod... methods) {
        return new RestPattern(path, Set.of(methods));
    }

    /**
     * Create a new instance of {@link RestPattern} with the given path and HTTP method Collections.
     */
    public static RestPattern of(final String path, final Collection<HttpMethod> methods) {
        return new RestPattern(path, new HashSet<>(methods));
    }

    /**
     * Create a new instance of {@link RestPattern} with the given path and HTTP method.
     */
    public static RestPattern of(final String path, final HttpMethod method) {
        return new RestPattern(path, Set.of(method));
    }

    public static RestPatternBuilder builder() {
        return new RestPatternBuilder();
    }

    /**
     * Compare the request URI and HTTP method.
     * <p> If the request URI and HTTP method match, return true.
     */
    boolean matches(final HttpServletRequest request) {
        return methods.contains(HttpMethod.valueOf(request.getMethod())) &&
                pathMatcher.match(path, request.getRequestURI());
    }

    String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestPattern that)) {
            return false;
        }

        if (!methods.equals(that.methods)) {
            return false;
        }
        return Objects.equals(path, that.path) && Objects.equals(methods, that.methods);
    }

    @Override
    public int hashCode() {
        int result = methods.hashCode();
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RestPattern{" +
                "methods=" + methods +
                ", path=" + path +
                '}';
    }

    /**
     * Builder for {@link RestPattern}.
     * <p> This class is used to create a new instance of {@link RestPattern}.
     * <p> Can set HTTP methods easily.
     */
    public static class RestPatternBuilder {

        private final Set<HttpMethod> methods = new HashSet<>();
        private String path = "/**";

        public RestPatternBuilder() {
        }

        public RestPatternBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RestPatternBuilder get() {
            this.methods.add(GET);
            return this;
        }

        public RestPatternBuilder post() {
            this.methods.add(HttpMethod.POST);
            return this;
        }

        public RestPatternBuilder put() {
            this.methods.add(HttpMethod.PUT);
            return this;
        }

        public RestPatternBuilder delete() {
            this.methods.add(HttpMethod.DELETE);
            return this;
        }

        public RestPatternBuilder patch() {
            this.methods.add(HttpMethod.PATCH);
            return this;
        }

        public RestPatternBuilder trace() {
            this.methods.add(HttpMethod.TRACE);
            return this;
        }

        public RestPatternBuilder options() {
            this.methods.add(HttpMethod.OPTIONS);
            return this;
        }

        public RestPatternBuilder head() {
            this.methods.add(HttpMethod.HEAD);
            return this;
        }

        public RestPatternBuilder all() {
            return get().post().put().delete().patch().trace().options().head();
        }

        public RestPattern build() {
            if (methods.isEmpty()) {
                return new RestPattern(path, Set.of(HttpMethod.values()));
            }
            return new RestPattern(path, methods);
        }
    }
}

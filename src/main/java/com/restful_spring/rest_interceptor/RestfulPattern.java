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
public final class RestfulPattern {

    private final String path;
    private final Set<HttpMethod> methods;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private RestfulPattern(final String path, final Set<HttpMethod> methods) {
        this.path = path;
        this.methods = methods;
    }

    /**
     * Create a new instance of {@link RestfulPattern} with the given path.
     * <p> Default HTTP methods are GET, POST, PUT, DELETE, PATCH, TRACE, OPTIONS, HEAD.
     *
     * @author cookie-meringue
     * @since 1.0
     */
    public static RestfulPattern fromPath(final String path) {
        return new RestfulPattern(path, Set.of(HttpMethod.values()));
    }

    /**
     * Create a new instance of {@link RestfulPattern} with the given path and HTTP methods.
     *
     * @author cookie-meringue
     * @since 1.0
     */
    public static RestfulPattern of(final String path, final HttpMethod... methods) {
        return new RestfulPattern(path, Set.of(methods));
    }

    /**
     * Create a new instance of {@link RestfulPattern} with the given path and HTTP method Collections.
     */
    public static RestfulPattern of(final String path, final Collection<HttpMethod> methods) {
        return new RestfulPattern(path, new HashSet<>(methods));
    }

    /**
     * Create a new instance of {@link RestfulPattern} with the given path and HTTP method.
     */
    public static RestfulPattern of(final String path, final HttpMethod method) {
        return new RestfulPattern(path, Set.of(method));
    }

    public static RestfulPatternBuilder builder() {
        return new RestfulPatternBuilder();
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
        if (!(o instanceof RestfulPattern that)) {
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
        return "RestfulPattern{" +
            "methods=" + methods +
            ", path=" + path +
            '}';
    }

    /**
     * Builder for {@link RestfulPattern}.
     * <p> This class is used to create a new instance of {@link RestfulPattern}.
     * <p> Can set HTTP methods easily.
     */
    public static class RestfulPatternBuilder {

        private final Set<HttpMethod> methods = new HashSet<>();
        private String path = "/**";

        public RestfulPatternBuilder() {
        }

        public RestfulPatternBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RestfulPatternBuilder get() {
            this.methods.add(GET);
            return this;
        }

        public RestfulPatternBuilder post() {
            this.methods.add(HttpMethod.POST);
            return this;
        }

        public RestfulPatternBuilder put() {
            this.methods.add(HttpMethod.PUT);
            return this;
        }

        public RestfulPatternBuilder delete() {
            this.methods.add(HttpMethod.DELETE);
            return this;
        }

        public RestfulPatternBuilder patch() {
            this.methods.add(HttpMethod.PATCH);
            return this;
        }

        public RestfulPatternBuilder trace() {
            this.methods.add(HttpMethod.TRACE);
            return this;
        }

        public RestfulPatternBuilder options() {
            this.methods.add(HttpMethod.OPTIONS);
            return this;
        }

        public RestfulPatternBuilder head() {
            this.methods.add(HttpMethod.HEAD);
            return this;
        }

        public RestfulPatternBuilder all() {
            return get().post().put().delete().patch().trace().options().head();
        }

        public RestfulPattern build() {
            if (methods.isEmpty()) {
                return new RestfulPattern(path, Set.of(HttpMethod.values()));
            }
            return new RestfulPattern(path, methods);
        }
    }
}

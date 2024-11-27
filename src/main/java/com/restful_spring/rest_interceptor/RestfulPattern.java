package com.restful_spring.rest_interceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriTemplate;

public class RestfulPattern {

    private final UriTemplate path;
    private final Set<HttpMethod> methods;

    private RestfulPattern(final UriTemplate path, final Set<HttpMethod> methods) {
        this.path = path;
        this.methods = methods;
    }

    public static RestfulPattern of(final String path, final Collection<HttpMethod> methods) {
        return new RestfulPattern(new UriTemplate(path), new HashSet<>(methods));
    }

    public static RestfulPattern of(final String path, final HttpMethod method) {
        return new RestfulPattern(new UriTemplate(path), Set.of(method));
    }

    public static RestfulPatternBuilder builder() {
        return new RestfulPatternBuilder();
    }

    public boolean matches(final HttpServletRequest request) {
        return methods.contains(HttpMethod.valueOf(request.getMethod())) && path.matches(request.getRequestURI());
    }

    public String getPath() {
        return path.toString();
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
                ", path=" + path.toString() +
                '}';
    }

    public static class RestfulPatternBuilder {

        private final Set<HttpMethod> methods;
        private UriTemplate path;

        public RestfulPatternBuilder() {
            this.path = new UriTemplate("/**");
            this.methods = new HashSet<>();
        }

        public RestfulPatternBuilder path(String path) {
            this.path = new UriTemplate(path);
            return this;
        }

        public RestfulPatternBuilder get() {
            this.methods.add(HttpMethod.GET);
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

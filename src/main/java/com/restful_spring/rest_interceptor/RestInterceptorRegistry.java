package com.restful_spring.rest_interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Helps with configuring a list of RestInterceptors. Adaptor between InterceptorRegistry and RestInterceptor.
 *
 * @author Dh3356
 * @since 0.1
 */
public class RestInterceptorRegistry {

    private final InterceptorRegistry registry;
    private final List<RestInterceptorRegistration> registrations = new ArrayList<>();

    public RestInterceptorRegistry(InterceptorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Adds the provided {@link RestInterceptor}.
     *
     * @param restInterceptor the restInterceptor to add
     * @return an {@link RestInterceptorRegistration} that allows you optionally configure the registered
     * restInterceptor further for example adding RestfulPatterns it should apply to.
     */
    public RestInterceptorRegistration addInterceptor(RestInterceptor restInterceptor) {
        RestInterceptorRegistration registration = new RestInterceptorRegistration(restInterceptor);
        registrations.add(registration);
        return registration;
    }

    /**
     * Reflects the registrations in the registry. This method should be called after all the registrations are done.
     * <p>Will be deprecated in the future.
     */
    public void build() {
        this.registrations.forEach(registration -> {
            RestInterceptor restInterceptor = registration.getRestInterceptor();

            registry.addInterceptor(restInterceptor)
                    .addPathPatterns(restInterceptor.restfulPatterns.stream()
                            .map(RestfulPattern::getPath)
                            .collect(Collectors.toList()))
                    .order(registration.getOrder());
        });
    }
}

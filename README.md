# Rest Interceptor

## Description

Spring's Interceptor works based on URI matching.

However, when developing a Restful API server, there are times when you need to use different HTTP methods for the same
uri.

Example:

- `GET /memos`
- `POST /memos`

If you create an Interceptor to apply User authentication only to `POST /memos`, you would perform the following
procedures.

1. Create an Interceptor.
2. Register the Interceptor for `/memos` in the `addInterceptor()` of the `WebMvcConfigurer` implementation.
3. Implement it so that `GET /memos` passes in the `preHandle()` of the Interceptor.

like this:

```
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("GET") && request.getRequestURI().equals("/memos")) {
            return true;
        }
        // authentication logic
        return true;
    }
}

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/memos");
    }
}
```

I thought this method would increase the complexity in a Restful application.
So I created a RestInterceptor library that matches based on URI and HTTP Method rather than URI-based matching.

## Add the dependency (build.gradle)

```
repositories {
    ...
    maven { url "https://jitpack.io" }
}

dependencies {
    ...
    implementation 'com.github.Dh3356:rest_interceptor:v{version}'
    // example: implementation 'com.github.Dh3356:rest_interceptor:v0.1'
}

// Since 1.0.3
repositories {
    ...
    maven { url "https://jitpack.io" }
}

dependencies {
    ...
    implementation 'com.github.cookie-meringue:rest_interceptor:v{version}'
    // example: implementation 'com.github.cookie-meringue:rest_interceptor:v1.0.3'
}
```

## Sample Code

### Assume we log `GET /memos requests`.

### We do not log `POST /memos` .

### 1. Create Interceptor witch extends `RestInterceptor`.

```
@Slf4j
@Component
public class RestTestInterceptor extends RestInterceptor {

    @Override
    protected boolean doInternal(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Hello, world!");
        return true;
    }
}
```

### 2. Add Interceptor in the implementation configuration class of WebMvcConfigurer

```
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RestTestInterceptor testInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Create RestInterceptorRegistry from InterceptorRegistry
        RestInterceptorRegistry restInterceptorRegistry = new RestInterceptorRegistry(registry);

        restInterceptorRegistry.addInterceptor(testInterceptor)
                .addRestfulPatterns(RestfulPattern.of("/memos", HttpMethod.GET)); // GET /memos

        restInterceptorRegistry.build(); // Deprecated Since v1.0
    }
}
```

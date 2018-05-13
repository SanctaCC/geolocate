package com.geolocation.mongodb.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
class MvcConfiguration implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;

    private final Integer pageableMaxPageSize;

    public MvcConfiguration(UserInterceptor userInterceptor,@Value("${maxPageSize:100}") Integer pageableMaxPageSize) {
        this.userInterceptor = userInterceptor;
        this.pageableMaxPageSize = pageableMaxPageSize;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).excludePathPatterns("/error");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addRedirectViewController("/","/index.html");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableConfig = new PageableHandlerMethodArgumentResolver();
        pageableConfig.setMaxPageSize(this.pageableMaxPageSize);
        resolvers.add(pageableConfig);
    }

    private PathResourceResolver getCustomResourceResolver(){
        return new PathResourceResolver() {
            @Override
            protected Resource getResource(String resourcePath,
                                           Resource location) throws IOException {
                Resource superR = super.getResource(resourcePath,location);
                return superR!=null ? superR
                        : new ClassPathResource("static/index.html");
            }
        };
    }

    private HttpRequestHandler customResourceHttpRequestHandler(ApplicationContext context) throws Exception {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocationValues(Collections.singletonList("classpath:/static/"));
        handler.setResourceResolvers(Collections.singletonList(getCustomResourceResolver()));
        handler.setResourceTransformers(Collections.singletonList(new AppCacheManifestTransformer()));
        handler.setCacheControl(CacheControl.maxAge(1,TimeUnit.DAYS));
        String[] allHTTPMethods = Arrays.stream(RequestMethod.values()).map(Enum::toString).toArray(String[]::new);
        //return index.html for every possible request except api calls
        handler.setSupportedMethods(allHTTPMethods);
        handler.setApplicationContext(context);
        handler.afterPropertiesSet();
        return handler;
    }

    @Bean
    public HandlerMapping customHandlerMapping(ApplicationContext applicationContext) throws Exception {
        Assert.notNull(applicationContext,"ApplicationContext null");
        HttpRequestHandler handler = customResourceHttpRequestHandler(applicationContext);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        //api calls should always have priority so set lowest possible precedence
        handlerMapping.setOrder(Integer.MAX_VALUE-1);
        handlerMapping.setUrlMap(Collections.singletonMap("/**",handler));
        handlerMapping.setInterceptors(this.userInterceptor);
        return handlerMapping;
    }

}
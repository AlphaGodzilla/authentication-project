package com.github.alphagodzilla.authentication.spring.boot.starter;

import com.github.alphagodzilla.authentication.core.AuthenticationManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author AlphaGodzilla
 * @date 2022/3/14 17:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "authentication.mvc.enable", havingValue = "true")
@ConditionalOnClass({HandlerInterceptor.class, WebMvcConfigurer.class})
@ConditionalOnBean(AuthenticationManager.class)
@AutoConfigureAfter(AuthenticationAutoConfiguration.class)
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(AuthenticationConfigProperties.class)
public class AuthenticationMvcAutoConfiguration {
    /**
     * 配置认证拦截器
     * @return 默认认证拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationInterceptor authenticationInterceptor(AuthenticationManager authenticationManager,
                                                               AuthenticationConfigProperties properties) {
        return new AuthenticationInterceptor(authenticationManager, properties.getMvc().getHeader());
    }

    /**
     * 注入WebMvc配置类
     * @param authenticationInterceptor 认证拦截器
     * @param properties 配置参数
     * @return WebMvc配置类
     */
    @Bean
    public WebMvcConfigurer configurer(AuthenticationInterceptor authenticationInterceptor,
                                       AuthenticationConfigProperties properties) {
        return new MvcAddInterceptorConfigurer(properties, authenticationInterceptor);
    }

    /**
     * Mvc配置类
     */
    public static class MvcAddInterceptorConfigurer implements WebMvcConfigurer {
        private final AuthenticationConfigProperties properties;
        private final HandlerInterceptor interceptor;

        public MvcAddInterceptorConfigurer(AuthenticationConfigProperties properties, HandlerInterceptor interceptor) {
            this.properties = properties;
            this.interceptor = interceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            AuthenticationConfigProperties.Mvc mvc = properties.getMvc();
            Set<String> includePaths = mvc.getIncludePaths();
            Set<String> excludePaths = mvc.getExcludePaths();
            registry.addInterceptor(interceptor)
                    .addPathPatterns(new ArrayList<>(includePaths))
                    .excludePathPatterns(new ArrayList<>(excludePaths));
        }
    }
}

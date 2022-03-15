package com.github.alphagodzilla.authentication.spring.boot.starter;

import com.github.alphagodzilla.authentication.core.*;
import com.github.alphagodzilla.authentication.core.repository.AntiBruteCrackRepository;
import com.github.alphagodzilla.authentication.core.repository.AuthenticationTokenRepository;
import com.github.alphagodzilla.authentication.core.service.AuthenticationAntiBruteCrackService;
import com.github.alphagodzilla.authentication.core.service.AuthenticationTokenService;
import com.github.alphagodzilla.authentication.defaults.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author T.J
 * @date 2022/3/11 14:15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "authentication.enable", havingValue = "true")
@EnableConfigurationProperties(AuthenticationConfigProperties.class)
@AutoConfigureBefore(AuthenticationMvcAutoConfiguration.class)
public class AuthenticationAutoConfiguration {
    /**
     * 默认防暴破存储器
     * @return 基于内存的防暴破信息存储器
     */
    @Bean
    @ConditionalOnMissingBean
    public AntiBruteCrackRepository inMemoryAntiBruteCrackRepository() {
        return new InMemoryAntiBruteCrackRepository();
    }

    /**
     * 默认的防暴破服务
     * @param antiBruteCrackRepository 防暴破存储器
     * @param properties 配置
     * @return 默认暴破服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationAntiBruteCrackService defaultAntiBruteCrackService(
            AntiBruteCrackRepository antiBruteCrackRepository,
            AuthenticationConfigProperties properties
    ) {
        int maxCrackTimes = 10;
        Duration banDuration = Duration.ofDays(1);
        AuthenticationConfigProperties.AntiBruteCrack antiBruteCrack = properties.getAntiBruteCrack();
        if (antiBruteCrack != null && antiBruteCrack.getMaxCrackTimes() != null) {
            maxCrackTimes = antiBruteCrack.getMaxCrackTimes();
        }
        if (antiBruteCrack != null && antiBruteCrack.getBanDuration() != null) {
            banDuration = antiBruteCrack.getBanDuration();
        }
        AntiBruteCrackStrategy strategy = new AntiBruteCrackStrategy(maxCrackTimes, banDuration);
        return new DefaultAuthenticationAntiBruteCrackService(antiBruteCrackRepository, strategy);
    }

    /**
     * 默认的HMac算法实现
     * @return HmacSha256算法实现
     */
    @Bean
    @ConditionalOnMissingBean
    public HmacAlgorithm hmacSha256Algorithm() {
        return new HmacSha256Algorithm();
    }

    /**
     * 默认的内存Token存储器
     * @return 内存存储器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationTokenRepository inMemoryAuthenticationTokenRepository() {
        return new InMemoryAuthenticationTokenRepository();
    }

    /**
     * Token管理服务
     * @param hmacAlgorithm Hmac算法实现
     * @param authenticationTokenRepository Token存储器
     * @param jsonParser Json解析器
     * @return 默认的Token管理服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationTokenService authenticationTokenService(HmacAlgorithm hmacAlgorithm,
                                                                 AuthenticationTokenRepository authenticationTokenRepository,
                                                                 JsonParser jsonParser
    ) {
        return new DefaultAuthenticationTokenService(hmacAlgorithm, authenticationTokenRepository, jsonParser);
    }

    /**
     * 默认密码编码器
     * @return 默认的基于BCrypt算法的密码编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder authenticationPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 创建认证管理器
     * @param antiBruteCrackService 防暴破管理服务
     * @param tokenService token管理服务
     * @param passwordEncoder 密码编码器
     * @return 认证管理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationAntiBruteCrackService antiBruteCrackService,
                                                       AuthenticationTokenService tokenService,
                                                       PasswordEncoder passwordEncoder) {
        return AuthenticationManagerBuilder.builder()
                .antiBruteCrackService(antiBruteCrackService)
                .tokenService(tokenService)
                .passwordEncoder(passwordEncoder)
                .build();
    }
}

package io.github.alphagodzilla.authentication.spring.boot.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Set;

/**
 * @author AlphaGodzilla
 * @date 2021/10/20 14:19
 */
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationConfigProperties {
    /**
     * 是否启用自动配置
     */
    private Boolean enable = false;
    /**
     * 防暴破配置
     */
    private AntiBruteCrack antiBruteCrack;

    /**
     * mvc配置
     */
    private Mvc mvc;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public AntiBruteCrack getAntiBruteCrack() {
        return antiBruteCrack;
    }

    public void setAntiBruteCrack(AntiBruteCrack antiBruteCrack) {
        this.antiBruteCrack = antiBruteCrack;
    }

    public Mvc getMvc() {
        return mvc;
    }

    public void setMvc(Mvc mvc) {
        this.mvc = mvc;
    }

    /**
     * 反暴破配置
     */
    public static class AntiBruteCrack {
        /**
         * 最大暴破次数
         */
        Integer maxCrackTimes = 10;
        /**
         * 禁用时长
         */
        Duration banDuration = Duration.ofDays(1);

        public Integer getMaxCrackTimes() {
            return maxCrackTimes;
        }

        public void setMaxCrackTimes(Integer maxCrackTimes) {
            this.maxCrackTimes = maxCrackTimes;
        }

        public Duration getBanDuration() {
            return banDuration;
        }

        public void setBanDuration(Duration banDuration) {
            this.banDuration = banDuration;
        }
    }

    /**
     * MVC配置
     */
    public static class Mvc {
        /**
         * 是否开启
         */
        private Boolean enable = false;
        /**
         * 请求头
         */
        private String header;
        /**
         * 过滤请求路径
         */
        private Set<String> includePaths;
        /**
         * 排除的请求路径
         */
        private Set<String> excludePaths;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public Set<String> getIncludePaths() {
            return includePaths;
        }

        public void setIncludePaths(Set<String> includePaths) {
            this.includePaths = includePaths;
        }

        public Set<String> getExcludePaths() {
            return excludePaths;
        }

        public void setExcludePaths(Set<String> excludePaths) {
            this.excludePaths = excludePaths;
        }
    }
}

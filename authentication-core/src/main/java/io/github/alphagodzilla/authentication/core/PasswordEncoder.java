package io.github.alphagodzilla.authentication.core;

/**
 * 密码编码器
 * @author AlphaGodzilla
 * @date 2022/3/9 17:50
 */
public interface PasswordEncoder {
    /**
     * 编码密码
     * @param rawPassword 原始密码
     * @return 编码后的密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 原始密码和编码密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 编码密码
     * @return 是否匹配
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}

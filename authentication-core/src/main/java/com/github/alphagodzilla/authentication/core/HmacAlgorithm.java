package com.github.alphagodzilla.authentication.core;

/**
 * HMac算法实现
 * @author AlphaGodzilla
 * @date 2022/3/11 11:08
 */
public interface HmacAlgorithm {
    /**
     * Hmac输出签名
     * @param secret 签名密钥
     * @param message 签名数据
     * @return 16进制编码签名
     */
    byte[] digest(byte[] secret, byte[] message);
}

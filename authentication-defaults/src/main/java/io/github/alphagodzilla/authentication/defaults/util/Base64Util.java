package io.github.alphagodzilla.authentication.defaults.util;

import java.nio.charset.StandardCharsets;

/**
 * @author AlphaGodzilla
 * @date 2022/3/11 15:05
 */
public class Base64Util {
    private static final byte[] DECODE_TABLE = {
            // 0 1 2 3 4 5 6 7 8 9 A B C D E F
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 00-0f
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 10-1f
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, // 20-2f + - /
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, // 30-3f 0-9，-2的位置是'='
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // 40-4f A-O
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, // 50-5f P-Z _
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, // 60-6f a-o
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 // 70-7a p-z
    };
    /**
     * 检查是否为Base64
     *
     * @param base64 Base64的bytes
     * @return 是否为Base64
     * @since 5.7.5
     */
    public static boolean isBase64(CharSequence base64) {
        if (base64 == null || base64.length() < 2) {
            return false;
        }

        final byte[] bytes = base64.toString().getBytes(StandardCharsets.UTF_8);

        if (bytes.length != base64.length()) {
            // 如果长度不相等，说明存在双字节字符，肯定不是Base64，直接返回false
            return false;
        }

        return isBase64(bytes);
    }

    /**
     * 检查是否为Base64
     *
     * @param base64Bytes Base64的bytes
     * @return 是否为Base64
     * @since 5.7.5
     */
    public static boolean isBase64(byte[] base64Bytes) {
        boolean hasPadding = false;
        for (byte base64Byte : base64Bytes) {
            if (hasPadding) {
                if ('=' != base64Byte) {
                    // 前一个字符是'='，则后边的字符都必须是'='，即'='只能都位于结尾
                    return false;
                }
            } else if ('=' == base64Byte) {
                // 发现'=' 标记之
                hasPadding = true;
            } else if (!(isBase64Code(base64Byte) || isWhiteSpace(base64Byte))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWhiteSpace(byte byteToCheck) {
        switch (byteToCheck) {
            case ' ':
            case '\n':
            case '\r':
            case '\t':
                return true;
            default:
                return false;
        }
    }

    public static boolean isBase64Code(byte octet) {
        return octet == '=' || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
    }
}

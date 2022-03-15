package com.github.alphagodzilla.authentication.defaults;

import com.github.alphagodzilla.authentication.core.HmacAlgorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author AlphaGodzilla
 * @date 2022/3/11 14:31
 */
public class HmacSha256Algorithm implements HmacAlgorithm {
    public static final String ALGORITHM = "HmacSHA256";

    @Override
    public byte[] digest(byte[] secret, byte[] message) {
        try {
            Mac hmacSha256 = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret, ALGORITHM);
            hmacSha256.init(secretKeySpec);
            return hmacSha256.doFinal(message);
        }catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new RuntimeException(exception);
        }
    }
}

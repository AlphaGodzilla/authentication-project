package io.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 14:14
 */
public class IllegalTokenException extends BaseAuthenticationException {
    public IllegalTokenException() {
        super("illegal token");
    }
}

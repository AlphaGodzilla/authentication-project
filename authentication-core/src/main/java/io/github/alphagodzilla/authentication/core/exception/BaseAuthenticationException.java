package io.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/19 14:21
 */
public class BaseAuthenticationException extends RuntimeException {
    public BaseAuthenticationException() {
        super();
    }

    public BaseAuthenticationException(String message) {
        super(message);
    }

    public BaseAuthenticationException(Throwable e) {
        super(e);
    }

    public BaseAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

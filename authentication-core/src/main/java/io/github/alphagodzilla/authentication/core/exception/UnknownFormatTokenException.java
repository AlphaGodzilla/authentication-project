package io.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 16:21
 */
public class UnknownFormatTokenException extends BaseAuthenticationException {
    private final static String DEFAULT_MESSAGE = "unknown format token";

    public UnknownFormatTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public UnknownFormatTokenException(Throwable e) {
        super(DEFAULT_MESSAGE, e);
    }
}

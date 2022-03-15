package io.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 14:04
 */
public class BannedException extends BaseAuthenticationException {
    public BannedException() {
        super("banned");
    }
}

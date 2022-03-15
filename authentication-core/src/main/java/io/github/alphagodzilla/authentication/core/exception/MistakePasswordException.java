package io.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 13:53
 */
public class MistakePasswordException extends BaseAuthenticationException {
    public MistakePasswordException() {
        super("mistake password");
    }
}

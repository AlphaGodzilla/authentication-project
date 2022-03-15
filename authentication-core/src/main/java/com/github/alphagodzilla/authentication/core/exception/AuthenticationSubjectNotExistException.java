package com.github.alphagodzilla.authentication.core.exception;

/**
 * @author AlphaGodzilla
 * @date 2021/10/14 11:59
 */
public class AuthenticationSubjectNotExistException extends BaseAuthenticationException {
    public AuthenticationSubjectNotExistException() {
        super("authenticated subject not exist");
    }
}

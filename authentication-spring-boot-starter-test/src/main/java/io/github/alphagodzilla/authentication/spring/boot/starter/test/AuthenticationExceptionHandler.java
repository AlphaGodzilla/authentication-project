package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import io.github.alphagodzilla.authentication.core.exception.BaseAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 12:01
 */
@Slf4j
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    public static class ErrorMsg {
        private final String error;

        public ErrorMsg(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BaseAuthenticationException.class)
    public ErrorMsg handBaseAuthenticationException(BaseAuthenticationException exception) {
        String msg = exception.getMessage();
        String exceptionSimpleName = exception.getClass().getSimpleName();
        log.warn(exceptionSimpleName + ":" + msg);
        return new ErrorMsg(msg);
    }
}

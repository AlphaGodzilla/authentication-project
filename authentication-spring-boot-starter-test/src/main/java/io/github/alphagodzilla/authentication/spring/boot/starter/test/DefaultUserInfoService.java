package io.github.alphagodzilla.authentication.spring.boot.starter.test;

import io.github.alphagodzilla.authentication.core.exception.AuthenticationSubjectNotExistException;
import io.github.alphagodzilla.authentication.core.model.UserInformation;
import io.github.alphagodzilla.authentication.core.service.AuthenticationUserService;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AlphaGodzilla
 * @date 2022/3/16 11:14
 */
@Component
public class DefaultUserInfoService implements AuthenticationUserService {
    private static final Map<String, String> embeddedUserPasswordMap = new HashMap<>();

    static {
        embeddedUserPasswordMap.put("user1", "$2a$10$2vGQ795aTsrV0fHTldrlxezs8QQcnwWlEbZCg2Tq8LpTISyXSBM8e");
        embeddedUserPasswordMap.put("user2","$2a$10$5dyqM4gpzj1JWMbXsWBO6OzJFinSEp6ARzW9a3qAn7QHt1fOoNWtG");
    }

    @Override
    public UserInformation getUserInformation(Serializable uid) throws AuthenticationSubjectNotExistException {
        String userPwd = embeddedUserPasswordMap.get(uid.toString());
        if (userPwd == null) {
            throw new AuthenticationSubjectNotExistException();
        }
        Map<String, String> attributes = new HashMap<>();
        attributes.put("uid",uid.toString());
        attributes.put("username",uid.toString());
        return new UserInformation(uid, uid.toString(), userPwd, attributes, new HashMap<>());
    }
}

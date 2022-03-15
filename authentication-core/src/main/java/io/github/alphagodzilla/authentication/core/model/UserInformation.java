package io.github.alphagodzilla.authentication.core.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author T.J
 * @date 2021/10/14 11:29
 */
public class UserInformation {
    private Serializable uid;
    private String username;
    private String encodedPassword;
    private Map<String, String> attributes;
    private Map<String, String> extraAttributes;

    public UserInformation(Serializable uid, String username, String encodedPassword,
                           Map<String, String> attributes, Map<String, String> extraAttributes) {
        this.uid = uid;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.attributes = attributes;
        this.extraAttributes = extraAttributes;
    }

    public Serializable getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setUid(Serializable uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getExtraAttributes() {
        return extraAttributes;
    }

    public void setExtraAttributes(Map<String, String> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }
}

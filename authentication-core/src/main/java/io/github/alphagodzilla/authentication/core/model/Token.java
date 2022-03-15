package io.github.alphagodzilla.authentication.core.model;

import java.util.Map;

/**
 * @author T.J
 * @date 2021/10/14 15:40
 */
public class Token {
    private String uid;
    private String sign;
    private Map<String, String> attributes;

    public Token() {
    }

    public Token(String uid, String sign) {
        this(uid, sign, null);
    }

    public Token(String uid, String sign, Map<String, String> attributes) {
        this.uid = uid;
        this.sign = sign;
        this.attributes = attributes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}

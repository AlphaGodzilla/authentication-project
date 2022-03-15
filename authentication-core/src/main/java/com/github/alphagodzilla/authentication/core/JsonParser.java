package com.github.alphagodzilla.authentication.core;

/**
 * Json解析器接口
 * @author T.J
 * @date 2022/2/24 20:37
 */
public interface JsonParser {
    /**
     * 解析为Java对象
     * @param jsonStr json字符串
     * @param tClass 目标Java类
     * @param <T> 泛型
     * @return Java对象
     */
    <T> T toBean(String jsonStr, Class<T> tClass) throws JsonParseException;

    /**
     * Java对象序列化为json字符串
     * @param object Java对象
     * @return json字符串
     */
    String toJsonString(Object object) throws JsonParseException;

    class JsonParseException extends RuntimeException{
        public JsonParseException(Throwable cause) {
            super(cause);
        }
    }
}

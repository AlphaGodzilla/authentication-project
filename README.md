# Authentication

该工程抽象了App开发中常用的认证功能，做到的自由扩展开箱即用，减少重复编码，提高开发效率。

## 项目组成
- `authentication-core`：核心接口类模块
- `authentication-defaults`：默认实现，提供了基础的json解析，编编码，基于内存的存储类实现
- `authentication-spring-boot-starter`：适配了spring boot start，提供了开箱即用的功能
### 依赖关系
`authentication-core` <-- `authentication-defaults` <-- `authentication-spring-boot-starter`

## SpringBoot快速集成
1. 加入依赖（Maven）
```xml
<dependency>
  <groupId>io.github.alphagodzilla</groupId>
  <artifactId>authentication-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. 应用配置
```yaml
# application.yml
# 认证模块开始配置
authentication:
  # 是否启用认证模块的自动配置
  enable: true
  # 反暴破配置
  antiBruteCrack:
    # 最大尝试授权次数
    maxCrackTimes: 10
    # 如果触发反暴破规则，则禁用账号的时长
    banDuration: 1d
  # MVC配置
  mvc:
    # 是否启用MVC的自动配置
    enable: true
    # 认证凭证的请求头
    header: Authentication
    # 包含认证检查的接口路径
    includePaths: ['/api/**']
    # 排除认证检查的接口路径
    excludePaths: ['/api/public/**']
```
3. 实现的`AuthenticationUserService`接口对接的当前业务系统，并注入到IOC容器中
```java
@Component
public class AuthenticationUserServiceImpl implements AuthenticationUserService {
    @Override
    public UserInformation getUserInformation(Serializable uid) throws AuthenticationSubjectNotExistException {
        // 查询用户信息，并包装成UserInformation对象
        if (userInformation == null) {
            throw new AuthenticationSubjectNotExistException();
        }
        return userInformation;
    }

}
```
4. AuthenticationManager
`AuthenticationManager`是全局认证的管理器。所有认证相关的功能都可以通过调用认证管理器的方法来完成
```java
public class AuthenticationManager {
    /**
     * 用户登录时调用改方法给用户颁发授权凭证
     * @param username 用户名
     * @param password 密码
     * @param authenticationUserService 用户服务
     * @return token
     */
    public TokenResult grant(String username, Duration retention, String password, AuthenticationUserService authenticationUserService) {
        // ...
    }

    /**
     * 检查Token是否为合约的Token
     * @param token 授权凭证
     */
    public void match(String token) { 
        // ...
    }

    /**
     * 刷新凭证
     * @param token 当前颁发的token
     * @param retention 颁发新token的有效期
     * @return 新的token
     */
    public TokenResult refresh(String token, Duration retention) {
        // ...
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revokeByUid(String uid) {
        // ...
    }

    /**
     * 吊销认证
     * @return 是否成功
     */
    public boolean revoke(String token) {
        // ...
    }

    /**
     * 解析token中的userId
     * @param token token
     * @return 用户ID
     */
    public String parseUid(String token) throws UnknownFormatTokenException {
        // ...
    }
}
```

## 自定义扩展点
模块提供许多扩展接口允许用户自定义。

### 存储类的自定义
默认的实现中，`AuthenticationTokenRepository`和`AntiBruteCrackRepository`是基于内存的实现。这样的实现只能用于本地运行或测试环境，
不能用于生产环境。生产环境中后端存储可能使用Redis或者MySQL等数据库进行存储，所以需要自定义实现存储类
##### 自定义Token存储
实现`AuthenticationTokenRepository`即可实现扩展
```java
public interface AuthenticationTokenRepository {
    /**
     * 保存token
     * @param key 键名
     * @param tokenPersistent 数据体
     * @return 是否成功
     */
    boolean putToken(Serializable key, TokenPersistent tokenPersistent);

    /**
     * 获取token
     * @param key 键名
     * @return 数据体
     */
    TokenPersistent getToken(Serializable key);

    /**
     * 删除token
     * @param key 键名
     * @return 是否成功
     */
    boolean deleteToken(Serializable key);
}
```
#### 自定义防暴破存储
实现`AntiBruteCrackRepository`即可实现扩展
```java
public interface AntiBruteCrackRepository {
    /**
     * 增加爆破记录数
     * @param key 键名
     * @param times 需要增加次数
     * @param retention 缓存时长
     * @return 增加后最新的总次数
     */
    int plusAndGetCrackTimes(Serializable key, int times, Duration retention);

    /**
     * 从禁用名单中删除
     * @param key 键名
     * @return 是否删除成功
     */
    boolean deleteBan(Serializable key);

    /**
     * 是否在禁用中
     * @param key 键名
     * @param maxCrackTimes 最大可破解次数
     * @return 是否存在
     */
    boolean hasBan(Serializable key, int maxCrackTimes);
}
```

### 自定义HMac签名算法
实现该接口可以自定义HMac签名算法。默认的实现是`HmacSha256Algorithm`
```java
public interface HmacAlgorithm {
    /**
     * Hmac输出签名
     * @param secret 签名密钥
     * @param message 签名数据
     * @return 16进制编码签名
     */
    byte[] digest(byte[] secret, byte[] message);
}
```

### 自定义密码编码器
实现该接口可以自定义密码编码器。默认实现是`BCryptPasswordEncoder`
```java
public interface PasswordEncoder {
    /**
     * 编码密码
     * @param rawPassword 原始密码
     * @return 编码后的密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 原始密码和编码密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 编码密码
     * @return 是否匹配
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
```

### 自定义JSON解析器
实现该接口可以自定义Json的解析实现。默认实现是`JacksonJsonParser`
```java
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
}
```

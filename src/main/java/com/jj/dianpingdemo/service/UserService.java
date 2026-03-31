package com.jj.dianpingdemo.service;

//import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.entity.UserDto;
import com.jj.dianpingdemo.mapper.UserMapper;
import com.jj.dianpingdemo.util.SessionConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author: JSY
 * @version: 1.0
 */
@Service
public class UserService {
    //private static final String MOCK_CODE = "123456";
    //private static final String SESSION_USER_KEY = "loginUser";
    private static final long LOGIN_CODE_TTL_MINUTES = 2L;
    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;

    // MVP：先用内存存验证码，后续替换为 Redis
    // private final Map<String, String> codeStore = new ConcurrentHashMap<>();


    public UserService(UserMapper userMapper, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

//    public LoginResult sendCode(String phone) {
//        if (phone == null || phone.length() != 11) {
//            return new LoginResult(false, "手机号格式不正确");
//        }
//        String code = String.format("%06d", new Random().nextInt(1_000_000));
//        codeStore.put(phone, code);
//
//        // mock 短信发送：控制台打印验证码
//        System.out.println("[MockSMS] phone=" + phone + ", code=" + code);
//
//        return new LoginResult(true, "验证码发送成功（mock）");
//    }
//
//    public LoginResult login(String phone, String code) {
//        if (phone == null || code == null) {
//            return new LoginResult(false, "手机号或验证码不能为空");
//        }
//        String cachedCode = codeStore.get(phone);
//        if (cachedCode == null) {
//            return new LoginResult(false, "请先获取验证码");
//        }
//        if (!cachedCode.equals(code)) {
//            return new LoginResult(false, "验证码错误");
//        }
//        return new LoginResult(true, "登录成功（mock）");
//    }
    public boolean sendCode(String phone) {
        if (phone == null || !phone.matches("^1\\d{10}$")) {
            return false;
        }
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        //codeStore.put(phone, code);
        stringRedisTemplate.opsForValue().set(
                SessionConstants.LOGIN_CODE_KEY_PREFIX + phone,
                code,
                SessionConstants.LOGIN_CODE_TTL.toMinutes(),
                TimeUnit.MINUTES
        );
//        String codeKey = SessionConstants.LOGIN_CODE_KEY_PREFIX + phone;
//
//        // 写入 Redis，并设置过期时间（2分钟）
//        stringRedisTemplate.opsForValue().set(codeKey, code, LOGIN_CODE_TTL_MINUTES, TimeUnit.MINUTES);

        // mock 短信发送：控制台打印验证码
        System.out.println("[MockSMS] phone=" + phone + ", code=" + code);
        return true;
    }

//    public boolean login(String phone, String code, HttpSession session) {
//        if (phone == null || phone.length() != 11) {
//            return false;
//        }
//        if (code == null || code.length() != 6) {
//            return false;
//        }
//        //String cachedCode = codeStore.get(phone);
//        String codeKey = SessionConstants.LOGIN_CODE_KEY_PREFIX + phone;
//        String cachedCode = stringRedisTemplate.opsForValue().get(codeKey);
//        if (cachedCode == null || !cachedCode.equals(code)) {
//            return false;
//        }
//        // MVP：先按手机号查用户，不存在则创建可后续补
//        User user = userMapper.selectByPhone(phone);
//        //System.out.println("[login] sessionId=" + session.getId() + ", user=" + user);
//        if (user == null) {
//            // 不存在则自动注册
//            user = new User();
//            user.setPhone(phone);
//            user.setNickName("user_" + phone.substring(phone.length() - 4));
//            userMapper.insert(user);
//        }
//        session.setAttribute(SessionConstants.LOGIN_USER_KEY, user);
//        //System.out.println("[login] afterSet=" + session.getAttribute(SessionConstants.LOGIN_USER_KEY));
//        // 验证码一次性使用，防止重复登录滥用
//        //codeStore.remove(phone);
//        return true;
//    }
public String login(String phone, String code) {
    if (phone == null || !phone.matches("^1\\d{10}$")) {
        return null;
    }

    // ===================== 临时关闭：注释掉验证码校验 =====================
    // String cacheCode = stringRedisTemplate.opsForValue().get(SessionConstants.LOGIN_CODE_KEY_PREFIX + phone);
    // if (cacheCode == null || !cacheCode.equals(code)) {
    //     return null;
    // }
    // ====================================================================

    User user = userMapper.selectByPhone(phone);
    if (user == null) {
        user = new User();
        user.setPhone(phone);
        user.setNickName("user_" + UUID.randomUUID().toString().substring(0, 8));
        userMapper.insert(user);
    }

    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setPhone(user.getPhone());
    dto.setNickName(user.getNickName());

    String token = UUID.randomUUID().toString().replace("-", "");
    String tokenKey = SessionConstants.LOGIN_USER_KEY + token;

    Map<String, String> map = new HashMap<>();
    map.put("id", String.valueOf(dto.getId()));
    map.put("phone", dto.getPhone());
    map.put("nickName", dto.getNickName());

    stringRedisTemplate.opsForHash().putAll(tokenKey, map);
    stringRedisTemplate.expire(tokenKey, SessionConstants.LOGIN_USER_TTL.toMinutes(), TimeUnit.MINUTES);

    // 验证码一次性消费
    // 验证码已关闭，删除逻辑也注释
    // stringRedisTemplate.delete(SessionConstants.LOGIN_CODE_KEY_PREFIX + phone);
    return token;
}

    public User currentUser(HttpSession session) {
        Object val = session.getAttribute(SessionConstants.LOGIN_USER_KEY);
        //System.out.println("[me] sessionId=" + session.getId() + ", valueType=" + (val == null ? "null" : val.getClass().getName()) + ", value=" + val);
        if (val instanceof User user) {
            return user;
        }
        return null;
    }

//    public void logout(HttpSession session) {
//        session.invalidate();
//    }
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        stringRedisTemplate.delete(SessionConstants.LOGIN_USER_KEY + token);
    }
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setPhone(user.getPhone());
        dto.setNickName(user.getNickName());
        return dto;
    }
}

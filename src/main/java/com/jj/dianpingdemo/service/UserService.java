package com.jj.dianpingdemo.service;

//import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.entity.UserDto;
import com.jj.dianpingdemo.mapper.UserMapper;
import com.jj.dianpingdemo.util.SessionConstants;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: JSY
 * @version: 1.0
 */
@Service
public class UserService {
    private static final String MOCK_CODE = "123456";
    //private static final String SESSION_USER_KEY = "loginUser";

    private final UserMapper userMapper;

    // 先用内存模拟验证码存储，下一步切到 Redis
//    private final Map<String, String> codeStore = new ConcurrentHashMap<>();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        // MVP：先 mock，后续接入真实短信服务
        return phone != null && phone.length() == 11;
    }

    public boolean login(String phone, String code, HttpSession session) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        if (!MOCK_CODE.equals(code)) {
            return false;
        }
        // MVP：先按手机号查用户，不存在则创建可后续补
        User user = userMapper.selectByPhone(phone);
        System.out.println("[login] sessionId=" + session.getId() + ", user=" + user);
        if (user == null) {
            return false;
        }
        session.setAttribute(SessionConstants.LOGIN_USER_KEY, user);
        System.out.println("[login] afterSet=" + session.getAttribute(SessionConstants.LOGIN_USER_KEY));
        return true;
    }

    public User currentUser(HttpSession session) {
        Object val = session.getAttribute(SessionConstants.LOGIN_USER_KEY);
        //System.out.println("[me] sessionId=" + session.getId() + ", valueType=" + (val == null ? "null" : val.getClass().getName()) + ", value=" + val);
        if (val instanceof User user) {
            return user;
        }
        return null;
    }

    public void logout(HttpSession session) {
        session.invalidate();
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

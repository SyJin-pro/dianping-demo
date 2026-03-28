package com.jj.dianpingdemo.controller;

//import com.jj.dianpingdemo.entity.LoginResult;
//import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.Result;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author: JSY
 * @version: 1.0
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping("/user/code")
    public Result<Void> sendCode(@RequestParam("phone") String phone) {
        boolean ok = userService.sendCode(phone);
        if (!ok) {
            return Result.fail("手机号格式不正确");
        }
        return Result.ok("验证码发送成功（mock）");
    }

    @PostMapping("/user/login")
    public Result<Void> login(@RequestParam("phone") String phone,
                              @RequestParam("code") String code,
                              HttpSession session) {
        boolean ok = userService.login(phone, code, session);
        if (!ok) {
            return Result.fail("手机号或验证码错误");
        }
        return Result.ok("登录成功（mock）");
    }

    @GetMapping("/user/me")
    public Result<User> me(HttpSession session) {
        User user = userService.currentUser(session);
        if (user == null) {
            return Result.fail("未登录");
        }
        return Result.ok("查询成功", user);
    }
}

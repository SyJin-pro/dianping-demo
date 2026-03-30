package com.jj.dianpingdemo.controller;

//import com.jj.dianpingdemo.entity.LoginResult;
//import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.Result;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.entity.UserDto;
import com.jj.dianpingdemo.service.UserService;
import com.jj.dianpingdemo.util.SessionConstants;
import com.jj.dianpingdemo.util.UserHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/user/login")
//    public Result<Void> login(@RequestParam("phone") String phone,
//                              @RequestParam("code") String code,
//                              HttpSession session) {
//        boolean ok = userService.login(phone, code, session);
//        if (!ok) {
//            return Result.fail("手机号或验证码错误");
//        }
//        return Result.ok("登录成功（mock）");
//    }
    @PostMapping("/user/login")
    public Result<String> login(@RequestParam("phone") String phone,
                                @RequestParam("code") String code) {
        String token = userService.login(phone, code);
        if (token == null) {
            return Result.fail("手机号或验证码错误");
        }
        return Result.ok("登录成功", token);
    }

//    @GetMapping("/user/me")
//    public Result<User> me(HttpSession session) {
//        User user = userService.currentUser(session);
//        if (user == null) {
//            return Result.fail("未登录");
//        }
//        return Result.ok("查询成功", user);
//    }

//    @GetMapping("/user/me")
//    public Result<UserDto> me() {
//        User user = UserHolder.getUser();
//        if (user == null) {
//            System.out.println("这里不应该出现，说明 LoginInterceptor 没有正确拦截到未登录的请求");
//            return Result.fail("未登录");
//        }
//        return Result.ok("查询成功", userService.toDto(user));
//    }
    @GetMapping("/user/me")
    public Result<UserDto> me() {
        UserDto user = (UserDto) UserHolder.getUser();
        if (user == null) {
            return Result.fail("未登录");
        }
        return Result.ok("查询成功", user);
    }

//    @PostMapping("/user/logout")
//    public Result<Void> logout(HttpSession session) {
//        userService.logout(session);
//        return Result.ok("退出登录成功");
//    }
    @PostMapping("/user/logout")
    public Result<Void> logout(@RequestHeader(value = SessionConstants.AUTHORIZATION_HEADER, required = false) String token) {
        userService.logout(token);
        return Result.ok("退出成功");
    }
}

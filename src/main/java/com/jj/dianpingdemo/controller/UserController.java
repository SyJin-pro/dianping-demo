package com.jj.dianpingdemo.controller;

import com.jj.dianpingdemo.entity.LoginResult;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.service.UserService;
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
    public LoginResult sendCode(@RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }

    @PostMapping("/user/login")
    public LoginResult login(@RequestParam("phone") String phone,
                             @RequestParam("code") String code) {
        return userService.login(phone, code);
    }
}

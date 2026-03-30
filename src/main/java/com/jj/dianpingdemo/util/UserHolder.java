package com.jj.dianpingdemo.util;

import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.entity.UserDto;

/**
 * @author: JSY
 * @version: 1.0
 */
public final class UserHolder {

    private static final ThreadLocal<UserDto> TL = new ThreadLocal<>();

    private UserHolder() {
    }

    public static void saveUser(UserDto user) {
        TL.set(user);
    }

    public static UserDto getUser() {
        return TL.get();
    }

    public static void removeUser() {
        TL.remove();
    }
}

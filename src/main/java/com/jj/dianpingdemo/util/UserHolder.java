package com.jj.dianpingdemo.util;

import com.jj.dianpingdemo.entity.User;

/**
 * @author: JSY
 * @version: 1.0
 */
public final class UserHolder {

    private static final ThreadLocal<User> TL = new ThreadLocal<>();

    private UserHolder() {
    }

    public static void saveUser(User user) {
        TL.set(user);
    }

    public static User getUser() {
        return TL.get();
    }

    public static void removeUser() {
        TL.remove();
    }
}

package com.winter.service;

import com.winter.model.User;

/**
 * Created by wjq
 */
public interface UserService {

    int addUser(User user);
    User selectUser(String account);

}


package com.winter.service;

//import com.github.pagehelper.PageHelper;
import com.winter.mapper.UserMapper;
import com.winter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;//这里会报错，但是并不会影响

    @Override
    public User selectUser(String account) {
        return userMapper.selectByPrimaryKey(account);
    }

    @Override
    public int addUser(User user) {

        return userMapper.insertSelective(user);
    }


}




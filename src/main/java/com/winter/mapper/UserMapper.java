package com.winter.mapper;

import com.winter.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String account);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String account);

    User selectByThreeKey(String account,String password,int type);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
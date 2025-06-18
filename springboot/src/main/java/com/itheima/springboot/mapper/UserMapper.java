package com.itheima.springboot.mapper;

import com.itheima.springboot.entities.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> findUserBySelectiveCriteria(User user);

}
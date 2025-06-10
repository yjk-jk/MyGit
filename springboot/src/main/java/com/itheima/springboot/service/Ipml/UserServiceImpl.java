package com.itheima.springboot.service.Ipml;

import com.itheima.springboot.mapper.UserMapper;
import com.itheima.springboot.entities.User;
import com.itheima.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    //使用@Resource注解注入UserDao
//    @Autowired
//    @Qualifier("myUserDao")

    @Autowired
    private UserMapper userMapper;


    public void save(User user){
        userMapper.insert(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }
}

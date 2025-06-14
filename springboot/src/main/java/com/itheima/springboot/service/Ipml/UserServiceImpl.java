package com.itheima.springboot.service.Ipml;

import com.itheima.springboot.entities.User;
import com.itheima.springboot.mapper.UserMapper;
import com.itheima.springboot.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Resource
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

    @Override
    public List<User> findByUser(User user) {
        return userMapper.findUserBySelectiveCriteria(user);
    }


    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void delete(String username) {
        User user = new User();
        user.setUsername(username);
        userMapper.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }
}

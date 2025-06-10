package com.itheima.springboot.service;


import com.itheima.springboot.entities.User;

public interface UserService {
    public void save(User user);
    public User findByUsername(String username);

}

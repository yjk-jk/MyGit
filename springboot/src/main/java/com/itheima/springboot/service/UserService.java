package com.itheima.springboot.service;


import com.itheima.springboot.entities.User;

import java.util.List;

public interface UserService {
    public void save(User user);
    List<User> findByUser(User user);

    void update(User user);

    void delete(String username);

    List<User> findAll();

    User findByUsername(String username);
}

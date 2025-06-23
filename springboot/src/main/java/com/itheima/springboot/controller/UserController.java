package com.itheima.springboot.controller;


import com.itheima.springboot.entities.User;
import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.resp.ReturnCodeEnum;
import com.itheima.springboot.service.UserService;
import com.itheima.springboot.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.itheima.springboot.utils.JwtUtil.EXPIRATION_TIME;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户模块", description = "用户相关接口")
public class UserController {
    //使用@Resource注解注入UserService
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //新增数据 一定是用post请求的

    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "新增用户接口")
    public ResultData addUser(@RequestBody User user){
        User user1 = userService.findByUsername(user.getUsername());
        if(user1!=null){
            return ResultData.fail(ReturnCodeEnum.UserAlreadyExist.getCode(),
                    ReturnCodeEnum.UserAlreadyExist.getMsg());
        }else {
        userService.save(user);
            return ResultData.success("新增成功");
        }
    }


    //是post请求，又是ajax application/json格式的数据的时候
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResultData login(@RequestBody User user){
        User user1=userService.findByUsername(user.getUsername()); //数据库里的密码是加密后的
        if(user1==null){
            return ResultData.fail(ReturnCodeEnum.UserNotExist.getCode(),
                    ReturnCodeEnum.UserNotExist.getMsg());

        }
        else if(user1.getPassword().equals(user.getPassword())){
            String token = JwtUtil.generateToken(user.getUsername());
            Map<String,Object> map=new HashMap<>();
            map.put("token",token);
            map.put("user",user1);
            //将token存入redis，key为username，value为token
            redisTemplate.opsForValue().set(user.getUsername(), token, EXPIRATION_TIME , TimeUnit.SECONDS);
            return ResultData.success(map);
        }
        else {
            return ResultData.fail(ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR.getCode(),
                    ReturnCodeEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());

        }
    }


    @PostMapping("/logout")
    @Operation(summary = "用户退出", description = "用户退出接口")
    public ResultData loginout(@RequestBody User user){
        //从redis中删除token
        redisTemplate.delete(user.getUsername());
        //返回成功
        return ResultData.success("退出成功");
    }

    // 修改用户信息
    @PutMapping("/update")
    @Operation(summary = "修改用户", description = "修改用户接口")
    public ResultData updateUser(@RequestBody User user) {
        User user1 = userService.findByUsername(user.getUsername());
        if (user1 == null) {
            return ResultData.fail(ReturnCodeEnum.UserNotExist.getCode(),
                    ReturnCodeEnum.UserNotExist.getMsg());
        }
        userService.update(user);
        return ResultData.success("修改成功");
    }

    // 查询所有用户信息
    @GetMapping("/all")
    @Operation(summary = "查询所有用户", description = "查询所有用户接口")
    public ResultData getAllUser(){
        return ResultData.success(userService.findAll());
    }

    // 查询用户信息
    @PostMapping("/get")
    @Operation(summary = "查询用户", description = "查询用户接口")
    public ResultData getUser(@RequestBody User user) {
        List<User> byUser = userService.findByUser(user);
        log.info("查询用户信息：{}" + byUser);
        if (byUser.size() == 0) {
            return ResultData.fail(ReturnCodeEnum.UserNotExist.getCode(),
                    ReturnCodeEnum.UserNotExist.getMsg());
        }
        return ResultData.success(byUser);
    }

    // 删除用户信息
    @DeleteMapping("/delete")
    @Operation(summary = "删除用户", description = "删除用户接口")
    public ResultData deleteUser(@RequestBody User user) {
        User user1 = userService.findByUsername(user.getUsername());
        if (user1 == null) {
            return ResultData.fail(ReturnCodeEnum.UserNotExist.getCode(),
                    ReturnCodeEnum.UserNotExist.getMsg());
        }
        userService.delete(user.getUsername());
        // 从redis中删除token
        redisTemplate.delete(user.getUsername());
        return ResultData.success("删除成功");
    }




}

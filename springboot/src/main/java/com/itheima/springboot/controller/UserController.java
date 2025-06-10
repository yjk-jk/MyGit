package com.itheima.springboot.controller;

import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.entities.User;
import com.itheima.springboot.resp.ReturnCodeEnum;
import com.itheima.springboot.service.UserService;
import com.itheima.springboot.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.itheima.springboot.utils.JwtUtil.EXPIRATION_TIME;


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
        }
        userService.save(user);
        return ResultData.success("新增成功");
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
}

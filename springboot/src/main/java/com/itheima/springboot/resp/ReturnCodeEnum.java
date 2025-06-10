package com.itheima.springboot.resp;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReturnCodeEnum {

    /**操作失败**/
    RC999("999","操作XXX失败"),
    /**操作成功**/
    RC200("200","success"),
    /**服务降级**/
    RC201("201","服务开启降级保护,请稍后再试!"),
    /**热点参数限流**/
    RC202("202","热点参数限流,请稍后再试!"),
    /**系统规则不满足**/
    RC203("203","系统规则不满足要求,请稍后再试!"),
    /**授权规则不通过**/
    RC204("204","授权规则不通过,请稍后再试!"),
    /**access_denied**/
    RC403("403","无访问权限,请联系管理员授予权限"),
    /**access_denied**/
    RC401("401","匿名用户访问无权限资源时的异常"),
    RC404("404","404页面找不到的异常"),
    /**服务异常**/
    RC500("500","系统异常，请稍后重试"),
    RC375("375","数学运算异常，请稍后重试"),

    INVALID_TOKEN("2001","访问令牌不合法"),
    ACCESS_DENIED("2003","没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED("1001","客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR("1002","用户名或密码错误"),
    BUSINESS_ERROR("1004","业务逻辑异常"),
    UserAlreadyExist("1005","用户已存在"),
    UserNotExist("1006","用户不存在"),
    UserLocked("1007","用户已被锁定"),
    UserExpired("1009","用户已过期"),
    UserCredentialsExpired("1010","用户凭证已过期"),
    UserNotAllowed("1011","用户不允许访问"),
    UNSUPPORTED_GRANT_TYPE("1003", "不支持的认证模式");



    private final String code;
    private final String msg;

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //传统bank-api接口返回码
    public static ReturnCodeEnum getReturnCOdeEnumV1(String code){
        for (ReturnCodeEnum e : ReturnCodeEnum.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return null;
    }

    //Stream流式接口返回码
    public static ReturnCodeEnum getReturnCOdeEnumV2(String code){
        return Arrays.stream(ReturnCodeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst()
                .orElse(null);

    }

    public static void main(String[] args) {
        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV1("200"));
        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV1("200").getCode());
        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV1("200").getMsg());

        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV2("1001"));
        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV2("1001").getCode());
        System.out.println(ReturnCodeEnum.getReturnCOdeEnumV2("1001").getMsg());
    }


}

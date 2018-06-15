package cn.haizhi.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    PARAM_ERROR(1, "参数错误"),
    USERNAME_HAD_EXIST(2, "用户名已存在"),
    LOGIN_FAIL(3, "用户名或密码错误"),
    USER_NOT_LOGIN(4, "用户未登录"),
    VALICADE_CODE_ERROR(5, "验证码错误"),
    PHONE_HAD_REGISTER(6, "该手机号码已注册"),
    USER_NOE_EXIST(7, "用户未注册"),
    ;
    private Integer code;
    private String message;

    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}

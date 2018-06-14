package cn.haizhi.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserAddForm {
    @Pattern(regexp = "^((17[0-9])|(14St [0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号码格式不正确")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotNull(message = "未选择性别")
    private Byte gender;

    @NotNull(message = "未填写年龄")
    private Byte age;
}

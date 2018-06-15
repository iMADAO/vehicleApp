package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserLoginForm {
    @NotBlank(message = "用户名或手机号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}

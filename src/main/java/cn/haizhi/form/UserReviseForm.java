package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserReviseForm {
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String code;
}

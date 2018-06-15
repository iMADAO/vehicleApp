package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UsernameForm {
    @NotBlank(message = "用户名不能为空")
    private String username;
}

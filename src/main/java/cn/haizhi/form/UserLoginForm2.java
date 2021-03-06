package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
public class UserLoginForm2 {
    @Pattern(regexp = "^((17[0-9])|(14St [0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号码格式不正确")
    @NotBlank(message = "手机号不能为空")
    private String phone;
    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "设备标识码不能为空")
    private String deviceCode;
}

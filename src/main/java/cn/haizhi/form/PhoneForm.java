package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PhoneForm {
    @NotBlank(message = "电话号码不能为空")
    private String phone;
}

package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class DeviceForm {
    @NotBlank(message = "设备id不能为空")
    private String deviceId;

    private String userId;
}

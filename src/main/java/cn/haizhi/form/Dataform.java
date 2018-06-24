package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class Dataform {
    @NotNull(message = "温度为空")
    private Double temperature;

    @NotNull(message = "体重为空")
    private Double weight;

    @NotNull(message = "心跳为空")
    private Double heartbeat;

    @NotNull(message = "血压收缩压为空")
    private Double systolicPressure;

    @NotNull(message = "血压舒张压为空")
    private Double diastolicPressure;

    @NotNull(message = "血脂为空")
    private Double bloodFat;

    @NotBlank(message = "设备id不能为空")
    private String deviceCode;
}

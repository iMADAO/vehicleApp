package cn.haizhi.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Dataform {
    @NotNull(message = "温度为空")
    private Double temperature;

    @NotNull(message = "体重为空")
    private Double weight;

    @NotNull(message = "心跳为空")
    private Double heartbeat;

    @NotNull(message = "血压为空")
    private Double bloodPressure;

    @NotNull(message = "血脂为空")
    private Double bloodFat;
}

package cn.haizhi.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupDataDTO {
    private Double bloodFat;
    private Double heartbeat;
    private Double temperature;
    private Double weight;
    private Double systolicPressure;
    private Double diastolicPressure;
    private LocalDateTime localDateTime;
}

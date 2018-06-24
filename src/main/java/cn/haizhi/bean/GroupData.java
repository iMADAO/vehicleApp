package cn.haizhi.bean;

import lombok.Data;

@Data
public class GroupData {
    private double bloodFat;
    private double heartbeat;
    private double temperature;
    private double weight;
//    private double bloodPressure;
    private BloodPressure bloodPressure;
}

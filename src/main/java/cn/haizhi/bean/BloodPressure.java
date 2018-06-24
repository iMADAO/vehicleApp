package cn.haizhi.bean;

import lombok.Data;

@Data
public class BloodPressure {
    private double diastolicPressure;
    private double systolicPressure;

    public BloodPressure(double systolicPressure, double diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
        this.systolicPressure = systolicPressure;
    }

    public BloodPressure() {
    }
}
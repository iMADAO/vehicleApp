package cn.haizhi.bean;

import lombok.Data;

@Data
public class BloodPressureStandard {
    private int startAge;
    private int endAge;
    private int gender;
    private double value;

    public BloodPressureStandard(int startAge, int endAge, int gender, double value) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.gender = gender;
        this.value = value;
    }

    public BloodPressureStandard() {
    }
}

package cn.haizhi.enums;

import cn.haizhi.bean.BloodPressure;
import lombok.Getter;

@Getter
public enum  BloodPressureEnum {
    HIGH1("1级高血压", 140.0, 159.0, 90.0, 99.0),
    HIGH2("2级高血压", 160.0, 179.0, 100.0, 109.0),
    HIGH3("3级高血压", 180.0, null, 100.0, null),
    NORMAL("正常", null, 139.0, null, 89.0),
    LOW("血压较低", null, 90.0, null, 60.0),
    ;

    private String message;
    private Double lowDiastolicPressure;
    private Double lowSystolicPressure;
    private Double hightDiastolicPressure;
    private Double highSystolicPressure;


    BloodPressureEnum(String message,  Double lowSystolicPressure, Double highSystolicPressure, Double lowDiastolicPressure, Double hightDiastolicPressure) {
        this.message = message;
        this.lowDiastolicPressure = lowDiastolicPressure;
        this.lowSystolicPressure = lowSystolicPressure;
        this.hightDiastolicPressure = hightDiastolicPressure;
        this.highSystolicPressure = highSystolicPressure;
    }
}
